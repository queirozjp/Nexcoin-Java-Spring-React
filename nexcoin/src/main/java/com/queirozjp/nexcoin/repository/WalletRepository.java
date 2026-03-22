package com.queirozjp.nexcoin.repository;

import com.queirozjp.nexcoin.models.Block;
import com.queirozjp.nexcoin.models.User;
import com.queirozjp.nexcoin.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByAddress(String address);
}
