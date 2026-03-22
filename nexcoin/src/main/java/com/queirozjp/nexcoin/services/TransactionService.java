package com.queirozjp.nexcoin.services;

import com.queirozjp.nexcoin.dto.AddFundsRequest;
import com.queirozjp.nexcoin.dto.ApiMessageResponse;
import com.queirozjp.nexcoin.dto.TransactionRequest;
import com.queirozjp.nexcoin.exception.InsufficientBalanceException;
import com.queirozjp.nexcoin.exception.SelfTransferException;
import com.queirozjp.nexcoin.exception.ToAddressNotFoundException;
import com.queirozjp.nexcoin.models.Transaction;
import com.queirozjp.nexcoin.models.User;
import com.queirozjp.nexcoin.models.Wallet;
import com.queirozjp.nexcoin.repository.UserRepository;
import com.queirozjp.nexcoin.repository.WalletRepository;
import com.queirozjp.nexcoin.util.TransactionSignature;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class TransactionService {

    private final BlockchainService blockchainService;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public TransactionService(
            BlockchainService blockchainService,
            WalletRepository walletRepository,
            UserRepository userRepository
    ) {
        this.blockchainService = blockchainService;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    private List<Transaction> pendingTransactions = new ArrayList<>();

    public BigDecimal getAvailableBalance(String address){
        BigDecimal balance = blockchainService.calculateConfirmedBalance(address);
        for (Transaction t : pendingTransactions){
            if (address.equals(t.getFromAddress())){
                balance = balance.subtract(t.getCoinAmount());
            }
            if (address.equals(t.getToAddress())){
                balance = balance.add(t.getCoinAmount());
            }
        }
        return balance;
    }

    public ApiMessageResponse registerTransaction(TransactionRequest request, String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Não foi possivel verificar o usuário"));

        Wallet wallet = user.getWallet();
        String address = wallet.getAddress();

        BigDecimal balance = getAvailableBalance(address);
        if (balance.compareTo(request.amount()) < 0) {
            throw new InsufficientBalanceException("Saldo Insuficiente!");
        }

        if (address.equals(request.toAddress())){
            throw new SelfTransferException("You can't send funds to yourself!");
        }

        if(walletRepository.findByAddress(request.toAddress()).isEmpty()){
            throw new ToAddressNotFoundException("To address not found!");
        }

        String formattedAmount = request.amount().setScale(2).toPlainString();
        String data = address + request.toAddress() + formattedAmount;

        PublicKey publicKey = TransactionSignature.bytesToPublicKey(wallet.getPublicKey());

        System.out.println("BACK DATA: " + data);

        boolean isValid = TransactionSignature.verifyTransaction(
                data,
                request.signature(),
                publicKey
        );

        if(!isValid){
            throw new RuntimeException("Assinatura inválida");
        }

        Transaction tx = new Transaction();
        tx.setFromAddress(address);
        tx.setToAddress(request.toAddress());
        tx.setCoinAmount(request.amount());
        tx.setSig(request.signature());
        tx.setTime(Instant.now());

        pendingTransactions.add(tx);

        return new ApiMessageResponse(
                "Transação registrada com sucesso",
                data
        );
    }

    public ApiMessageResponse createRewardTransaction(AddFundsRequest request, String email){
        Transaction tx = new Transaction();
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("The user could not be verified"));
        Wallet wallet = user.getWallet();
        String address = wallet.getAddress();
        if(walletRepository.findByAddress(address).isEmpty()){
            throw new RuntimeException("Wallet not fount");
        }
        tx.setFromAddress(null);
        tx.setToAddress(address);
        tx.setCoinAmount(request.amount());
        tx.setSig(null);
        tx.setTime(Instant.now());
        pendingTransactions.add(tx);

        return new ApiMessageResponse(
                "Funds Added Successfully!",
                request.amount().toString()
        );
    }

    public List<Transaction> getPendingTransactions() { return pendingTransactions; }
}
