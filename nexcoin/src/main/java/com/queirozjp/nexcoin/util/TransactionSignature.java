package com.queirozjp.nexcoin.util;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.security.Signature;
import java.security.PublicKey;
import java.security.PrivateKey;

public class TransactionSignature{
    // Verify the signing of the transaction
    public static boolean verifyTransaction(String data, String signature, PublicKey publicKey) {
        try {
            Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] signatureBytes = Base64.getDecoder().decode(signature);

            return ecdsaVerify.verify(signatureBytes);
        } catch (Exception e) { throw new RuntimeException(e); }
    }


    public static PublicKey bytesToPublicKey(byte[] keyBytes) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            return keyFactory.generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}