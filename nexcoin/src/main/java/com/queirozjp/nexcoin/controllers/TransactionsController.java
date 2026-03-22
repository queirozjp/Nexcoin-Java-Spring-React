package com.queirozjp.nexcoin.controllers;

import com.queirozjp.nexcoin.dto.AddFundsRequest;
import com.queirozjp.nexcoin.dto.ApiMessageResponse;
import com.queirozjp.nexcoin.services.TransactionService;
import com.queirozjp.nexcoin.dto.TransactionRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {
    private final TransactionService transactionService;

    public TransactionsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/newtransaction")
    public ResponseEntity<ApiMessageResponse> registerTransaction(@RequestBody @Valid TransactionRequest request, Authentication authentication){
        ApiMessageResponse response = transactionService.registerTransaction(request, authentication.getName());
        return ResponseEntity.ok(response);

    }

    @PostMapping("/deposit")
    public ResponseEntity<ApiMessageResponse> addFunds(@RequestBody @Valid AddFundsRequest request, Authentication authentication){
        ApiMessageResponse response = transactionService.createRewardTransaction(request, authentication.getName());
        return ResponseEntity.ok(response);
    }



}
