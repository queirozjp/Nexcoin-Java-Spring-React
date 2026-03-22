package com.queirozjp.nexcoin.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "signature", nullable = true, length = 512)
    private String signature;

    @Column(name = "fromAddress", nullable = true, length = 512)
    private String fromAddress;

    @Column(name = "toAddress", nullable = false, length = 512)
    private String toAddress;

    @Column(name = "coin_amount", nullable = false)
    private BigDecimal coinAmount;

    @Column(name = "time", nullable = false)
    private Instant time;

    @ManyToOne
    @JoinColumn(name = "block_id")
    @JsonIgnore
    private Block block;

    public Transaction(){}
    public Long getId() { return id; }
    public String getToAddress() { return toAddress; }
    public String getFromAddress() { return fromAddress; }
    public BigDecimal getCoinAmount() { return coinAmount; }
    public Instant getTime(){ return time; }

    public void setBlock(Block block) { this.block = block; }
    public void setFromAddress(String fromAddress) { this.fromAddress = fromAddress; }
    public void setToAddress(String toAddress) { this.toAddress = toAddress; }
    public void setCoinAmount(BigDecimal coinAmount) { this.coinAmount = coinAmount; }
    public void setSig(String signature){ this.signature = signature; }
    public void setTime(Instant time) { this.time = time; }
}
