package com.queirozjp.nexcoin.services;

import com.queirozjp.nexcoin.dto.*;
import com.queirozjp.nexcoin.exception.EmailAlreadyExistsException;
import com.queirozjp.nexcoin.exception.InvalidPasswordException;
import com.queirozjp.nexcoin.models.Transaction;
import com.queirozjp.nexcoin.models.User;
import com.queirozjp.nexcoin.models.Wallet;
import com.queirozjp.nexcoin.repository.BlockchainRepository;
import com.queirozjp.nexcoin.repository.TransactionsRepository;
import com.queirozjp.nexcoin.repository.UserRepository;
import com.queirozjp.nexcoin.security.JwtService;
import com.queirozjp.nexcoin.util.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.KeyPair;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TransactionsRepository transactionsRepository;
    private final BlockchainRepository blockchainRepository;
    private final JwtService jwtService;
    private final BlockchainService blockchainService;
    private final TransactionService transactionService;

    public UserService(UserRepository userRepository,
                       TransactionsRepository transactionsRepository,
                       BlockchainRepository blockchainRepository,
                       JwtService jwtService,
                       TransactionService transactionService,
                       BlockchainService blockchainService) {
        this.userRepository = userRepository;
        this.transactionsRepository = transactionsRepository;
        this.blockchainRepository = blockchainRepository;
        this.jwtService = jwtService;
        this.blockchainService = blockchainService;
        this.transactionService = transactionService;
    }

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public RegisterResponse register(RegisterRequest request){
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }
        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setEmail(request.email());
        newUser.setPassword(encoder.encode(request.password()));
        newUser.setRole("USER");
        userRepository.save(newUser);

        KeyPair keyPair = KeyGenerator.generateKeyPair();

        Wallet newWallet = new Wallet();
        newWallet.setPublicKey(keyPair.getPublic().getEncoded());
        newWallet.setAddress(KeyGenerator.generateAddress(keyPair.getPublic()));
        String privateKey = KeyGenerator.privateKeyToHex(keyPair.getPrivate());

        newWallet.setUser(newUser);
        newUser.setWallet(newWallet);

        userRepository.save(newUser);
        return new RegisterResponse(
                "User successfully created. Keep your private key safe!",
                privateKey
        );
    }

    public LoginResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.email()).orElseThrow();
        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }
        return new LoginResponse(
                "Login in...",
                jwtService.generateToken(user.getEmail())
        );
    }

    public BigDecimal getUserBalance(String email){
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("User not found!"));
        Wallet wallet = user.getWallet();
        String address = wallet.getAddress();
        BigDecimal balance = blockchainService.calculateConfirmedBalance(address);
        for (Transaction t : transactionService.getPendingTransactions()){
            if (address.equals(t.getFromAddress())){
                balance = balance.subtract(t.getCoinAmount());
            }
            if (address.equals(t.getToAddress())){
                balance = balance.add(t.getCoinAmount());
            }
        }
        return balance;
    }

    public ApiMessageResponse getAddressAndPublicKey(String email){
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("Não foi possivel verificar o usuário"));
        Wallet wallet = user.getWallet();
        String address = wallet.getAddress();
        String publicKey =  Base64.getEncoder()
                .encodeToString(wallet.getPublicKey());
        return new ApiMessageResponse(
                address,
                publicKey
        );
    }

    public Long getNumberOfTransactions(String email){
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("Não foi possivel verificar o usuário"));
        Wallet wallet = user.getWallet();
        String address = wallet.getAddress();
        long ct = 0;
        for (Transaction tx : transactionService.getPendingTransactions()){
            if(address.equals(tx.getFromAddress()) || address.equals(tx.getToAddress())){
                ct++;
            }
        }
        return transactionsRepository.countTransactionsByAddress(address) + ct;
    }

    public Long blocksMined(String email){
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("Não foi possivel verificar o usuário"));
        Wallet wallet = user.getWallet();
        return blockchainRepository.countByWalletId(wallet.getId());
    }

    public ApiMessageResponse getUsernameAndEmail(String email){
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("Não foi possivel verificar o usuário"));
        String username = user.getUsername();
        return new ApiMessageResponse(
                email,
                username
        );
    }

    public List<TransactionResponse> getLastFiveTransactions(String email){
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("Não foi possivel verificar o usuário"));
        Wallet wallet = user.getWallet();
        String address = wallet.getAddress();
        List<TransactionResponse> result = new ArrayList<>();

        List<Transaction> confirmed = transactionsRepository
                .findTop5ByFromAddressOrToAddressOrderByTimeDesc(address, address);

        for (Transaction tx : confirmed) {

            String direction;

            if (address.equals(tx.getToAddress())) {
                direction = "receive";
            } else {
                direction = "send";
            }

            result.add(new TransactionResponse(
                    tx.getFromAddress(),
                    tx.getToAddress(),
                    tx.getCoinAmount(),
                    "confirmed",
                    tx.getTime(),
                    direction
            ));
        }

        for (Transaction tx : transactionService.getPendingTransactions()) {

            if(address.equals(tx.getFromAddress()) || address.equals(tx.getToAddress())){

                String type = address.equals(tx.getFromAddress()) ? "send" : "receive";

                result.add(new TransactionResponse(
                        tx.getFromAddress(),
                        tx.getToAddress(),
                        tx.getCoinAmount(),
                        "pending",
                        tx.getTime(),
                        type
                ));
            }
        }

        result.sort((a,b) -> b.time().compareTo(a.time()));
        return result.stream().limit(5).toList();
    }

    public List<TransactionResponse> getTransactionsHistory(String email, int page, int size){
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("Não foi possivel verificar o usuário"));
        Wallet wallet = user.getWallet();
        String address = wallet.getAddress();
        List<TransactionResponse> result = new ArrayList<>();

        Pageable pageable = PageRequest.of(page, size);

        Page<Transaction> confirmedPage =
                transactionsRepository.findByFromAddressOrToAddressOrderByTimeDesc(
                        address,
                        address,
                        pageable
                );

        List<Transaction> confirmed = confirmedPage.getContent();

        for (Transaction tx : confirmed) {

            String direction = address.equals(tx.getToAddress()) ? "receive" : "send";

            result.add(new TransactionResponse(
                    tx.getFromAddress(),
                    tx.getToAddress(),
                    tx.getCoinAmount(),
                    "confirmed",
                    tx.getTime(),
                    direction
            ));
        }

        if(page == 0){

            for (Transaction tx : transactionService.getPendingTransactions()) {

                if(address.equals(tx.getFromAddress()) || address.equals(tx.getToAddress())){

                    String direction = address.equals(tx.getToAddress()) ? "receive" : "send";

                    result.add(new TransactionResponse(
                            tx.getFromAddress(),
                            tx.getToAddress(),
                            tx.getCoinAmount(),
                            "pending",
                            tx.getTime(),
                            direction
                    ));
                }
            }
        }
        result.sort((a,b) -> b.time().compareTo(a.time()));
        return result;
    }
}
