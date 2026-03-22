package com.queirozjp.nexcoin.repository;

import com.queirozjp.nexcoin.models.User;
import com.queirozjp.nexcoin.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
