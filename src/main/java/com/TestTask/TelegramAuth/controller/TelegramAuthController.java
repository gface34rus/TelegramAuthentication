package com.TestTask.TelegramAuth.controller;

import com.TestTask.TelegramAuth.model.TelegramUser;
import com.TestTask.TelegramAuth.repository.TelegramUserRepository;
import com.TestTask.TelegramAuth.service.TelegramWebAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class TelegramAuthController {
    @Autowired
    private TelegramWebAppService telegramWebAppService;
    @Autowired
    private TelegramUserRepository userRepository;

    @GetMapping("/")
    public String showWebApp() {
        return "webapp";
    }

    @PostMapping("/auth")
    @ResponseBody
    public ResponseEntity<?> authenticateUser(@RequestBody String body) {
        Map<String, String> initData = new java.util.HashMap<>();
        try {
            // Пробуем распарсить как JSON
            if (body.trim().startsWith("{")) {
                initData = new com.fasterxml.jackson.databind.ObjectMapper().readValue(body, Map.class);
            } else {
                // Иначе парсим как query string
                for (String pair : body.split("&")) {
                    String[] kv = pair.split("=", 2);
                    if (kv.length == 2) initData.put(kv[0], java.net.URLDecoder.decode(kv[1], java.nio.charset.StandardCharsets.UTF_8));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка парсинга данных");
        }
        System.out.println("initData: " + initData);
        // Для валидации hash используем оригинальный map
        Map<String, String> originalInitData = new java.util.HashMap<>(initData);
        if (!telegramWebAppService.validateWebAppData(originalInitData)) {
            return ResponseEntity.badRequest().body("Ошибка аутентификации");
        }
        // Для сохранения пользователя распаковываем user
        if (initData.containsKey("user")) {
            try {
                Map<String, Object> userMap = new com.fasterxml.jackson.databind.ObjectMapper().readValue(initData.get("user"), Map.class);
                for (Map.Entry<String, Object> entry : userMap.entrySet()) {
                    if (entry.getValue() != null) {
                        initData.put(entry.getKey(), entry.getValue().toString());
                    }
                }
            } catch (Exception e) {
                System.out.println("Ошибка парсинга user: " + e.getMessage());
            }
        }
        try {
            TelegramUser user = new TelegramUser();
            user.setId(Long.parseLong(initData.get("id")));
            user.setUsername(initData.get("username"));
            user.setFirstName(initData.get("first_name"));
            user.setLastName(initData.get("last_name"));
            user.setPhotoUrl(initData.get("photo_url"));
            user.setLanguageCode(initData.get("language_code"));
            user.setAuthDate(Long.parseLong(initData.get("auth_date")));
            userRepository.save(user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка обработки пользователя");
        }
    }
}