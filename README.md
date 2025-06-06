# Telegram Web App Authentication

Проект реализует базовую аутентификацию пользователей через Telegram Web App с использованием Spring Boot.

## Функциональность

- Аутентификация пользователей через Telegram Web App
- Валидация данных initData от Telegram
- Отображение персональных данных пользователя
- Сохранение пользователей в базу данных
- Стилизация с использованием Tailwind CSS

## Требования

- Java 17
- Maven
- Node.js и npm (устанавливаются автоматически через Maven)

## Запуск проекта

1. Установите переменную окружения с токеном бота:
```bash
export TELEGRAM_BOT_TOKEN=ваш_токен_бота
```

2. Соберите проект:
```bash
mvn clean package
```

3. Запустите приложение:
```bash
java -jar target/TelegramAuth-0.0.1-SNAPSHOT.jar
```

## Деплой

### Локальное тестирование через ngrok

1. Установите ngrok
2. Запустите приложение
3. В новом терминале запустите:
```bash
ngrok http 8080
```
4. Используйте полученный HTTPS URL для настройки вашего бота

### Деплой на Railway

1. Установите Railway CLI:
```bash
npm i -g @railway/cli
```

2. Войдите в систему:
```bash
railway login
```

3. Создайте новый проект:
```bash
railway init
```

4. Добавьте переменную окружения:
```bash
railway variables set TELEGRAM_BOT_TOKEN=ваш_токен_бота
```

5. Деплой:
```bash
railway up
```

## Структура проекта

- `src/main/java/com/TestTask/TelegramAuth/` - исходный код
  - `controller/` - контроллеры
  - `model/` - модели данных
  - `repository/` - репозитории
  - `service/` - сервисы
- `src/main/resources/`
  - `templates/` - HTML шаблоны
  - `static/` - статические файлы
  - `application.properties` - конфигурация

## База данных

Используется встроенная база данных H2:
- Консоль: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:file:./telegramauth
- Username: sa
- Password: password 