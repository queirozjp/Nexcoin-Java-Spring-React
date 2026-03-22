package com.queirozjp.nexcoin.models;

import jakarta.persistence.*;
import jakarta.websocket.ClientEndpoint;

import java.security.PublicKey;
import java.util.List;


@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 128)
    private String address;

    @Lob
    @Column(name = "public_key", nullable = false)
    private byte[] publicKey;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "wallet")
    private List<Block> blocks;

    public Wallet() {}

    public String getAddress(){ return address; }
    public byte[] getPublicKey(){ return publicKey; }
    public Long getId(){ return id; }

    public void setAddress(String address) { this.address = address; }
    public void setPublicKey(byte[] publicKey) { this.publicKey = publicKey; }
    public void setUser(User user) { this.user = user; }
}
