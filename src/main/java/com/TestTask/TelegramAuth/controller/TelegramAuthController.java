package com.TestTask.TelegramAuth.controller;

import com.TestTask.TelegramAuth.model.User;
import com.TestTask.TelegramAuth.service.TelegramAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TelegramAuthController {
    @Autowired
    private TelegramAuthService telegramAuthService;

    @GetMapping("/")
    public String index(@RequestParam String first_name,
                        @RequestParam String last_name,
                        @RequestParam String username,
                        Model model) {
        System.out.println("Received data: first_name=" + first_name + ", last_name=" + last_name + ", username=" + username);

        User user = telegramAuthService.authenticateUser(first_name, last_name, username);

        if (user != null) {
            System.out.println("User found or created: " + user);
        } else {
            System.out.println("User not found.");
        }

        model.addAttribute("user", user);
        return "index";
    }
}

