package com.queirozjp.nexcoin.util;

import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

public class KeyGenerator {
    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC",  "BC");

            ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
            keyGen.initialize(ecSpec, new SecureRandom());

            return keyGen.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateAddress(PublicKey publicKey) {
        try {
            byte[] publicKeyBytes = publicKey.getEncoded();

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(publicKeyBytes);

            return bytesToHex(hash).substring(0, 40);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar address", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();

        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static String privateKeyToHex(PrivateKey privateKey) {
        ECPrivateKey ecPrivateKey = (ECPrivateKey) privateKey;
        return ecPrivateKey.getS().toString(16);
    }
}
