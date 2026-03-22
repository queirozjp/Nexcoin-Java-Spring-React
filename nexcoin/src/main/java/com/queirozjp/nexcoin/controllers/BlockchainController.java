package com.queirozjp.nexcoin.controllers;


import com.queirozjp.nexcoin.dto.ApiMessageResponse;
import com.queirozjp.nexcoin.models.Block;
import com.queirozjp.nexcoin.models.Transaction;
import com.queirozjp.nexcoin.services.BlockchainService;
import com.queirozjp.nexcoin.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blockchain")
public class BlockchainController {

    private final BlockchainService blockchainService;
    private final TransactionService transactionService;
    public BlockchainController(BlockchainService blockchainService,
                                TransactionService transactionService){
        this.blockchainService = blockchainService;
        this.transactionService = transactionService;
    }

    @PostMapping("/mine")
    public ResponseEntity<ApiMessageResponse> mineBlock(Authentication authentication){
        List<Transaction> pending = transactionService.getPendingTransactions();
        ApiMessageResponse response = blockchainService.mineBlock(pending, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chain")
    public List<Block> listBlockchain(){
        return blockchainService.listBlockchain();
    }



}
