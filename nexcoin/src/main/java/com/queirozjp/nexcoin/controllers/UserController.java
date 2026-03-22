package com.queirozjp.nexcoin.controllers;

import com.queirozjp.nexcoin.dto.ApiMessageResponse;
import com.queirozjp.nexcoin.dto.TransactionResponse;
import com.queirozjp.nexcoin.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(Authentication authentication){
         BigDecimal balance = userService.getUserBalance(authentication.getName());
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/complexdetails")
    public ResponseEntity<ApiMessageResponse> getAddressAndPublicKey(Authentication authentication){
        ApiMessageResponse response = userService.getAddressAndPublicKey(authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions/count")
    public ResponseEntity<Long> getNumberOfTransactions(Authentication authentication){
        Long count = userService.getNumberOfTransactions(authentication.getName());
        return ResponseEntity.ok(count);
    }

    @GetMapping("/blocks/count")
    public ResponseEntity<Long> getNumberOfMinedBlocks(Authentication authentication){
        Long count = userService.blocksMined(authentication.getName());
        return ResponseEntity.ok(count);
    }

    @GetMapping("/simpledetails")
    public ResponseEntity<ApiMessageResponse> getUsernameAndEmail(Authentication authentication){
        ApiMessageResponse response = userService.getUsernameAndEmail(authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions/latest")
    public ResponseEntity<List<TransactionResponse>>  getLatestTransactions(Authentication authentication){
        List<TransactionResponse> transactions = userService.getLastFiveTransactions(authentication.getName());
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions/history")
    public ResponseEntity<List<TransactionResponse>>  getAllTransactions(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "5") int size,
                                                                         Authentication authentication){
        List<TransactionResponse> transactions = userService.getTransactionsHistory(authentication.getName(), page, size);
        return ResponseEntity.ok(transactions);
    }
}
