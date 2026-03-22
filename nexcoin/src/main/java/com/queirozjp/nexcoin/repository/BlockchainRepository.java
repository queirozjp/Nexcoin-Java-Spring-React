package com.queirozjp.nexcoin.repository;

import com.queirozjp.nexcoin.models.Block;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlockchainRepository extends JpaRepository<Block, Long> {
    Block findTopByOrderByIdDesc();
    List<Block> findAllByOrderByIdAsc();
    List<Block> findAllByOrderByIdDesc();
    long countByWalletId(Long walletId);
}
