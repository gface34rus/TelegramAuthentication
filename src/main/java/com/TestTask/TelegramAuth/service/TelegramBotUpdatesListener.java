package com.TestTask.TelegramAuth.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final static Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    public static final Pattern DATE_VALIDATION_PATTERN =
            Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");
    private final TelegramBot bot;

    public TelegramBotUpdatesListener(TelegramBot bot) {
        this.bot = bot;
    }

    @PostConstruct
    public void init() {
        bot.setUpdatesListener(this);
    }


    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            String text = update.message().text();

            logger.info("Processing update text: {}", text);
            if (!isTextExist(update)) {
                logger.warn("Not expected message: {}", text);
                return;
            }
            Matcher matcher = DATE_VALIDATION_PATTERN.matcher(text);
            String chatId = getChatId(update);
            SendMessage sendMessage = null;

            if (text.equals("/start")) {
                sendMessage = new SendMessage(chatId, "WELCOME_MESSAGE");
            } else if (matcher.matches()) {
                try {
                    sendMessage = new SendMessage(chatId, "TASK_ADDED");
                } catch (DateTimeParseException e) {
                    logger.error("[{}]", e.getMessage());
                    sendMessage = new SendMessage(chatId, "Не корректная дата");
                }
            }
            if (sendMessage != null) {
                execute(sendMessage);
            }
            if (text.equals("/")) {

            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void execute(Collection<SendMessage> messages) {
        messages.forEach(bot::execute);

    }

    public void execute(SendMessage messages) {
        execute(List.of(messages));
    }


    private boolean isTextExist(Update update) {
        return update.message() != null && !update.message().text().isBlank();
    }

    private String getChatId(Update update) {
        if (update.message().chat() == null) {
            throw new IllegalArgumentException("Отсутствует chatId");
        }
        return String.valueOf(update.message().chat().id());
    }
}