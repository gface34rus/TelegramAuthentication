package com.TestTask.TelegramAuth.repository;

import com.TestTask.TelegramAuth.model.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
} 