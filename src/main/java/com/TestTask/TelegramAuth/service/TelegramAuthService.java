package com.TestTask.TelegramAuth.service;

import com.TestTask.TelegramAuth.model.User;
import com.TestTask.TelegramAuth.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

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


        String expectedSignature = generateSignature(payload, secret);
        if (!expectedSignature.equals(signature)) {
            return false;
        }


        String authDate = extractAuthDate(payload);
        if (authDate != null && isAuthDateExpired(authDate)) {
            return false;
        }

        return true;
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

    private String extractAuthDate(String payload) {
        try {

            String decodedPayload = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);


            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(decodedPayload);


            return jsonNode.path("auth_date").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract auth_date from payload", e);
        }
    }

    private boolean isAuthDateExpired(String authDate) {
        long currentTime = System.currentTimeMillis() / 1000;
        long authTime = Long.parseLong(authDate);
        return (currentTime - authTime) > 300;
    }
    public Map<String, String> parseInitData(String initData) {
        Map<String, String> dataMap = new TreeMap<>();
        String[] pairs = initData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                dataMap.put(keyValue[0], keyValue[1]);
            }
        }
        return dataMap;
    }

}