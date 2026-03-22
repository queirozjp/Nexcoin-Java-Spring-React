package com.queirozjp.nexcoin.services;

import com.queirozjp.nexcoin.dto.ApiMessageResponse;
import com.queirozjp.nexcoin.models.Block;
import com.queirozjp.nexcoin.models.Transaction;
import com.queirozjp.nexcoin.models.User;
import com.queirozjp.nexcoin.models.Wallet;
import com.queirozjp.nexcoin.repository.BlockchainRepository;
import com.queirozjp.nexcoin.repository.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlockchainService {

    private final BlockchainRepository blockchainRepository;
    private final UserRepository userRepository;
    public static final BigDecimal MINING_REWARD = BigDecimal.valueOf(10);;

    public BlockchainService(BlockchainRepository blockchain,
                             UserRepository userRepository) {
        this.blockchainRepository = blockchain;
        this.userRepository = userRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        if (blockchainRepository.count() == 0) {
            createGenesisBlock();
        }
    }

    public void createGenesisBlock(){
        Block genesis = new Block();
        genesis.setPreviousHash("0");
        genesis.setTime(Instant.now());
        genesis.setNonce(0);
        genesis.setTransactions(new ArrayList<>());
        String hash = computeHash(hashStringBuilder(genesis));
        genesis.setHash(hash);
        blockchainRepository.save(genesis);
    }

    public String hashStringBuilder(Block block){
        StringBuilder sb = new StringBuilder();
        sb.append(block.getId())
                .append(block.getPreviousHash())
                .append(block.getTime())
                .append(block.getNonce());
        for (Transaction tx : block.getTransactions()){
            sb.append(tx.getId())
                    .append(tx.getFromAddress())
                    .append(tx.getToAddress())
                    .append(tx.getCoinAmount());
        }
        return sb.toString();
    }

    public String computeHash(String in){
        try{
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            byte[] hash = instance.digest(in.getBytes("UTF-8"));
            StringBuilder hexadecimal = new StringBuilder();
            for (byte i : hash){
                String temp = Integer.toHexString(0xff & i);
                if (temp.length() == 1) hexadecimal.append('0');
                hexadecimal.append(temp);
            }
            return hexadecimal.toString();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BigDecimal calculateConfirmedBalance(String address) {
        BigDecimal balance = BigDecimal.ZERO;
        List<Block> chain = blockchainRepository.findAllByOrderByIdAsc();
        for (Block b : chain) {
            for (Transaction t : b.getTransactions()) {
                if (address.equals(t.getFromAddress())) {
                    balance = balance.subtract(t.getCoinAmount());
                }
                if (address.equals(t.getToAddress())) {
                    balance = balance.add(t.getCoinAmount());
                }

            }
        }
        return balance;
    }

    public ApiMessageResponse mineBlock(List<Transaction> transactions, String email){
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("Não foi possivel verificar o usuário"));
        Wallet wallet = user.getWallet();
        String address = wallet.getAddress();
        Block newBlock = new Block();
        Block previousBlock = blockchainRepository.findTopByOrderByIdDesc();

        if (transactions == null || transactions.isEmpty()){
                throw new IllegalStateException("No pending transactions to mine.");
        }

        newBlock.setPreviousHash(previousBlock.getHash());
        newBlock.setTime(Instant.now());

        for (Transaction tx : transactions) {
            tx.setBlock(newBlock);
        }

        newBlock.setTransactions(transactions);

        String hash = proofOfWork(4, newBlock);
        newBlock.setHash(hash);
        newBlock.setWallet(wallet);

        blockchainRepository.save(newBlock);
        transactions.clear();

        Transaction minedTx = new Transaction();
        minedTx.setFromAddress(null);
        minedTx.setToAddress(address);
        minedTx.setCoinAmount(MINING_REWARD);
        minedTx.setTime(Instant.now());
        minedTx.setSig(null);

        transactions.add(minedTx);

        return new ApiMessageResponse(
                "Bloco minerado com sucesso",
                "Sua recompensa foi computada"
        );
    }

    public String proofOfWork(int difficulty, Block newBlock){
        String target = "0".repeat(difficulty);
        String hash;
        int nonce = 0;
        while (true){
            newBlock.setNonce(nonce);
            nonce++;
            hash = computeHash(hashStringBuilder(newBlock));
            if (hash.startsWith(target)) {
                return hash;
            }
        }
    }

    public List<Block> listBlockchain(){
        return blockchainRepository.findAllByOrderByIdDesc();
    }

}
