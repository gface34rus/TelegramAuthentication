# TelegramAuth

## Описание
TelegramAuth - это приложение, которое использует механизм аутентификации Telegram для управления пользователями. Оно позволяет пользователям аутентифицироваться через Telegram, хранить их данные в базе данных и предоставлять веб-интерфейс для отображения информации о пользователе.

## Технологии
- **Spring Boot** 3.5.0
- **Spring Data JPA** 3.5.0
- **Spring Web** 3.5.0
- **H2 Database** 2.1.217
- **Thymeleaf** для шаблонизации
- **Java** 17

## Структура проекта
C:\Users\gface\IdeaProjects\TelegramAuth ├── .gitattributes ├── .gitignore ├── pom.xml ├── README.md ├── system.properties ├── tailwind.config.js └── src ├── main │ ├── resources │ │ ├── application.properties │ │ ├── application.yml │ │ └── templates │ │ ├── index.html │ │ ├── user-info.html │ │ └── webapp.html │ └── java │ └── com │ └── TestTask │ └── TelegramAuth │ ├── controller │ ├── model │ ├── repository │ └── service └── test └── java └── com └── TestTask └── TelegramAuth └── TelegramAuthApplicationTests.java

COPY

## Установка
1. Убедитесь, что у вас установлен [Java JDK](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) версии 17.
2. Установите [Maven](https://maven.apache.org/download.cgi) для управления зависимостями.
3. Клонируйте репозиторий:
   ```bash
   git clone <URL_вашего_репозитория>
Перейдите в директорию проекта:
cd TelegramAuth
Соберите проект с помощью Maven:
mvn clean install
Запуск
Для запуска приложения используйте следующую команду:
mvn spring-boot:run
Использование
После запуска приложения вы можете отправлять запросы к API для аутентификации пользователей через Telegram. Основные эндпоинты:

/ - Обрабатывает запросы аутентификации от Telegram.
/webapp - Обрабатывает запросы от веб-приложения.
Тестирование
Для тестирования функциональности проекта вы можете использовать Postman или cURL. Примеры запросов:
Аутентификация пользователя:
GET http://localhost:8080/?first_name=Имя&last_name=Фамилия&username=ИмяПользователя&data=Данные
Получение информации о пользователе:
GET http://localhost:8080/webapp?data=Данные
