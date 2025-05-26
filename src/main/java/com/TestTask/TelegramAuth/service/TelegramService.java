package com.TestTask.TelegramAuth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramService {
    @Value("${telegram.bot.token}")
    private String botToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessage(String chatId, String message) {
        String url = String.format("https://api.telegram.org/bot%s/sendMessage", botToken);
        String requestUrl = String.format("%s?chat_id=%s&text=%s", url, chatId, message);
        restTemplate.getForObject(requestUrl, String.class);
    }
}
