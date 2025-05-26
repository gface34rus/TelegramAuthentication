package com.TestTask.TelegramAuth.repository;

import com.TestTask.TelegramAuth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
