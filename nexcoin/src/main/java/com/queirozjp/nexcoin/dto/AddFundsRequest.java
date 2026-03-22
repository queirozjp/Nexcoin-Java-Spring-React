package com.queirozjp.nexcoin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.aspectj.weaver.ast.Not;

import java.math.BigDecimal;

public record AddFundsRequest(
        @Positive
        BigDecimal amount
) {}
