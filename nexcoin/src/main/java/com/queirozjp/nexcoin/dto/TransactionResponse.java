package com.queirozjp.nexcoin.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(
        String from,
        String to,
        BigDecimal amount,
        String status,
        Instant time,
        String direction
) {}
