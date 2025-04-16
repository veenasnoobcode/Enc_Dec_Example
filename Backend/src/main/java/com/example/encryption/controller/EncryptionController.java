package com.example.encryption.controller;

import com.example.encryption.model.EncryptionResponse;
import com.example.encryption.service.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class EncryptionController {

    @Autowired
    private EncryptionService encryptionService;

    @PostMapping(value = "/encrypt", consumes = "text/plain")
    public ResponseEntity<?> encrypt(@RequestBody(required = false) String text) {
        try {
            if (text == null || text.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Input text cannot be empty");
            }
            // Clean the input text
            text = text.trim();
            EncryptionResponse response = encryptionService.encrypt(text);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Encryption failed: " + e.getMessage());
        }
    }

    @PostMapping(value = "/decrypt", consumes = "text/plain")
    public ResponseEntity<?> decrypt(@RequestBody(required = false) String encryptedText) {
        try {
            if (encryptedText == null || encryptedText.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Input text cannot be empty");
            }
            // Clean the input text
            encryptedText = encryptedText.trim();
            EncryptionResponse response = encryptionService.decrypt(encryptedText);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Decryption failed: " + e.getMessage());
        }
    }
} 