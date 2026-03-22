package com.queirozjp.nexcoin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotBlank
        String toAddress,
        @Positive
        BigDecimal amount,
        @NotBlank
        String signature
) {}
