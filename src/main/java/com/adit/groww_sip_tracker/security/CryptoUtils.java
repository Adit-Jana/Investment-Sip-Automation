package com.adit.groww_sip_tracker.security;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Base64;

@Slf4j
public class CryptoUtils {

    private static final String ALGO = "AES";
    private static final Logger log = LoggerFactory.getLogger(CryptoUtils.class);

    /**
     * Encrypts a plain text secret using AES and returns a base64-encoded ciphertext.
     *
     * @param plainText The raw TOTP secret (as a string)
     * @param secretKey The AES SecretKey (typically loaded from keystore)
     * @return The encrypted secret, base64-encoded
     * @throws Exception If encryption fails
     */
    public static String encrypt(String plainText, SecretKey secretKey) throws Exception {
        // Create AES cipher instance
        Cipher cipher = Cipher.getInstance(ALGO);

        // Initialize cipher for encryption mode using the secret key
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Perform encryption on the plain text
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

        // Encode encrypted binary data to base64 so it's safe to store in .properties files
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /*public static String decryptOld(String cipherText, SecretKey secretKey) throws Exception {
        //byte[] key = Base64.getDecoder().decode(base64Key);
        //SecretKeySpec secretKey = new SecretKeySpec(key, ALGO);

        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(decrypted);
    }*/

    /**
     * Decrypts a base64-encoded cipher string using a provided AES SecretKey.
     *
     * @param cipherText The encrypted text (Base64)
     * @param secretKey  The AES SecretKey loaded from keystore
     * @return The decrypted plain text
     */
    public static String decrypt(String cipherText, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedCipher = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(decodedCipher);
            return new String(decrypted);
        } catch (Exception e) {
            log.error("Exception occurred due to", e);
        }
        return "";
    }


}
