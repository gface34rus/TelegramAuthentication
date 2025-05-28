package com.TestTask.TelegramAuth.controller;

import com.TestTask.TelegramAuth.model.User;
import com.TestTask.TelegramAuth.service.TelegramAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@Controller
public class TelegramAuthController {
    @Autowired
    private TelegramAuthService telegramAuthService;

    @GetMapping("/")
    public String authenticateUser(@RequestParam String initData, Model model) {
        if (telegramAuthService.validateTelegramData(initData, "${telegram.bot.token}")) {
            Map<String, String> dataMap = telegramAuthService.parseInitData(initData);

            // Создайте объект User и заполните его данными
            User user = new User();
            user.setFirstName(dataMap.get("first_name"));
            user.setLastName(dataMap.get("last_name"));
            user.setUsername(dataMap.get("username"));

            model.addAttribute("user", user);
            return "index"; // Возвращает HTML-шаблон с данными пользователя
        } else {
            return "error"; // Возвращает страницу ошибки
        }
    }
}