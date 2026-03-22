package com.queirozjp.nexcoin.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "blockchain")
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 512)
    private String hash;

    @Column(name = "previous_hash", nullable = false, length = 512)
    private String previousHash;

    @Column(nullable = false)
    private Instant time;

    @Column(name = "nonce", nullable = false)
    private int nonce;

    @OneToMany(mappedBy = "block", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Transaction> transactions;

    @ManyToOne
    @JoinColumn(name="wallet_id")
    private Wallet wallet;


    public Block() {}

    public Long getId() { return id; }
    public String getHash() { return hash; }
    public String getPreviousHash() { return previousHash; }
    public Instant getTime() { return time; }
    public int getNonce() { return nonce; }
    public List<Transaction> getTransactions() { return transactions; }

    public void setHash(String hash) { this.hash = hash; }
    public void setPreviousHash(String previousHash) { this.previousHash = previousHash; }
    public void setTime(Instant time) { this.time = time; }
    public void setNonce(int nonce) { this.nonce = nonce; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }
    public void setWallet(Wallet wallet) { this.wallet = wallet; }
}
