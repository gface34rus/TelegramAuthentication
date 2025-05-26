package com.TestTask.TelegramAuth.service;

import com.TestTask.TelegramAuth.model.User;
import com.TestTask.TelegramAuth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class TelegramAuthService {
    @Autowired
    private UserRepository userRepository;

    public User authenticateUser(String firstName, String lastName, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(username);
            userRepository.save(user);
        }
        return user;
    }

    public boolean validateTelegramData(String data, String secret) {
        String[] parts = data.split("\\.");
        if (parts.length != 2) {
            return false;
        }

        String payload = parts[0];
        String signature = parts[1];

        // Проверка подписи
        String expectedSignature = generateSignature(payload, secret);
        return expectedSignature.equals(signature);
    }

    private String generateSignature(String payload, String secret) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = payload + secret;
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
