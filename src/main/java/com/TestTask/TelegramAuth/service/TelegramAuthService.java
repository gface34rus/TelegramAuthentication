package com.TestTask.TelegramAuth.service;

import com.TestTask.TelegramAuth.model.User;
import com.TestTask.TelegramAuth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
