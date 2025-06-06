package com.TestTask.TelegramAuth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class TelegramWebAppService {

    @Value("${telegram.bot.token}")
    private String botToken;

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    private static final String WEB_APP_DATA_KEY = "WebAppData";
    private static final long MAX_AGE_SECONDS = TimeUnit.DAYS.toSeconds(1); // 24 hours

    public boolean validateWebAppData(Map<String, String> initData) {
        try {
            String hash = initData.remove("hash");
            if (hash == null) {
                System.out.println("[validateWebAppData] hash отсутствует");
                return false;
            }

            // Формируем data check string только из нужных полей
            List<String> dataCheckArray = new ArrayList<>();
            // Добавляем только те поля, которые нужны для проверки
            if (initData.containsKey("user")) {
                dataCheckArray.add("user=" + initData.get("user"));
            }
            if (initData.containsKey("chat_instance")) {
                dataCheckArray.add("chat_instance=" + initData.get("chat_instance"));
            }
            if (initData.containsKey("chat_type")) {
                dataCheckArray.add("chat_type=" + initData.get("chat_type"));
            }
            if (initData.containsKey("auth_date")) {
                dataCheckArray.add("auth_date=" + initData.get("auth_date"));
            }
            Collections.sort(dataCheckArray);
            String dataCheckString = String.join("\n", dataCheckArray);

            // Validate auth_date
            String authDateStr = initData.get("auth_date");
            if (authDateStr != null) {
                long authDate = Long.parseLong(authDateStr);
                long currentTime = Instant.now().getEpochSecond();
                if (currentTime - authDate > MAX_AGE_SECONDS) {
                    System.out.println("[validateWebAppData] auth_date устарел");
                    return false;
                }
            }

            // Генерируем секретный ключ из токена бота
            byte[] secretKey = generateSecretKey();
            System.out.println("[validateWebAppData] secretKey (hex): " + bytesToHex(secretKey));

            // Вычисляем HMAC-SHA-256
            String calculatedHash = calculateHmacSha256(dataCheckString, secretKey);
            System.out.println("[validateWebAppData] dataCheckString:\n" + dataCheckString);
            System.out.println("[validateWebAppData] hash (от Telegram): " + hash);
            System.out.println("[validateWebAppData] calculatedHash:      " + calculatedHash);
            return hash.equals(calculatedHash);
        } catch (Exception e) {
            System.out.println("[validateWebAppData] Exception: " + e.getMessage());
            return false;
        }
    }

    private byte[] generateSecretKey() throws NoSuchAlgorithmException, InvalidKeyException {
        // Используем часть токена после двоеточия
        String tokenPart = botToken.substring(botToken.indexOf(':') + 1);
        System.out.println("[generateSecretKey] Используем часть токена после двоеточия: " + tokenPart);
        
        // Создаем HMAC-SHA-256 с токеном как ключом
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(
                tokenPart.getBytes(StandardCharsets.UTF_8),
                HMAC_SHA256_ALGORITHM
        );
        mac.init(secretKeySpec);
        
        // Используем "WebAppData" как данные для генерации ключа
        byte[] secretKey = mac.doFinal(WEB_APP_DATA_KEY.getBytes(StandardCharsets.UTF_8));
        return secretKey;
    }

    private String calculateHmacSha256(String data, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        System.out.println("[calculateHmacSha256] data bytes (hex): " + bytesToHex(data.getBytes(StandardCharsets.UTF_8)));
        System.out.println("[calculateHmacSha256] key (hex): " + bytesToHex(key));
        
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, HMAC_SHA256_ALGORITHM);
        mac.init(secretKeySpec);
        byte[] hmacSha256 = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        String result = bytesToHex(hmacSha256);
        System.out.println("[calculateHmacSha256] result: " + result);
        return result;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
} 