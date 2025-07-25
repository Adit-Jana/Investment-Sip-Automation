package com.adit.groww_sip_tracker.security;

import javax.crypto.SecretKey;

public class EncryptDecryptWrapper {

    // AES key
    SecretKey aesKey = KeystoreLoader.loadKey("secure/keystore.jceks", "changeit", "aesKey");

    public String encryptRawSecretWrapper() {

        try {
            // Load raw secret from file or vault
            String rawSecret = SecretSourceReader.getRawTotpSecret()
                    .orElseThrow(() -> new RuntimeException("TOTP secret not found"));

            // Load AES key from keystore
            //SecretKey key = KeystoreLoader.loadKey("secure/keystore.jceks", "changeit", "aesKey");

            // Encrypt and print
            String encrypted = CryptoUtils.encrypt(rawSecret, aesKey);
            System.out.println("✅ Encrypted TOTP Secret:\n" + encrypted);
            return encrypted;
        } catch (Exception e) {
            System.err.println("❌ Encryption failed: " + e.getMessage());
            return "";
        }
    }

    public String decryptionSecretWrapper(String encryptedText) {

        try {
            String decrypt = CryptoUtils.decrypt(encryptedText, aesKey);
            System.out.println("✅ Decrypted TOTP Secret:\n" + decrypt);
            return decrypt;
        } catch (Exception e) {
            System.err.println("❌ Decryption failed: " + e.getMessage());
            return "";
        }
    }
}
