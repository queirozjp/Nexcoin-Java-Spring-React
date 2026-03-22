package com.queirozjp.nexcoin.repository;

import com.queirozjp.nexcoin.models.Transaction;
import com.queirozjp.nexcoin.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionsRepository extends JpaRepository<Transaction, String> {
    @Query("""
    SELECT COUNT(t)
    FROM Transaction t
    WHERE t.fromAddress = :address
       OR t.toAddress = :address
    """)
    long countTransactionsByAddress(String address);
    List<Transaction> findTop5ByFromAddressOrToAddressOrderByTimeDesc(String from, String to);
    Page<Transaction> findByFromAddressOrToAddressOrderByTimeDesc(
            String from,
            String to,
            Pageable pageable
    );
}
