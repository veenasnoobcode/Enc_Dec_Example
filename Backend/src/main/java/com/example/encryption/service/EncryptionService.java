package com.example.encryption.service;

import com.example.encryption.model.EncryptionResponse;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.security.MessageDigest;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String KEY_ALGORITHM = "AES";
    private static final String KEY = "veena12345678901"; // Making it 16 bytes to secure the encryption
    private static final String CHARSET = "UTF-8";

    private SecretKeySpec generateKey() throws Exception {
        byte[] key = KEY.getBytes(StandardCharsets.UTF_8);
        // Ensure key is exactly 16 bytes
        byte[] fixedKey = new byte[16];
        System.arraycopy(key, 0, fixedKey, 0, Math.min(key.length, 16));
        // If key is less than 16 bytes, pad with zeros
        for (int i = key.length; i < 16; i++) {
            fixedKey[i] = 0;
        }
        return new SecretKeySpec(fixedKey, KEY_ALGORITHM);
    }

    public EncryptionResponse encrypt(String value) throws Exception {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be empty");
        }
        try {
            SecretKeySpec keySpec = generateKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedBytes = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            String encryptedValue = Base64.getEncoder().encodeToString(encryptedBytes);
            return new EncryptionResponse(KEY, encryptedValue);
        } catch (Exception e) {
            throw new RuntimeException("Error during encryption: " + e.getMessage(), e);
        }
    }

    public EncryptionResponse decrypt(String encryptedValue) throws Exception {
        if (encryptedValue == null || encryptedValue.isEmpty()) {
            throw new IllegalArgumentException("Encrypted string cannot be empty");
        }
        try {
            SecretKeySpec keySpec = generateKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            String decryptedValue = new String(decryptedBytes, StandardCharsets.UTF_8);
            return new EncryptionResponse(KEY, decryptedValue);
        } catch (Exception e) {
            throw new RuntimeException("Error during decryption: " + e.getMessage(), e);
        }
    }
} 