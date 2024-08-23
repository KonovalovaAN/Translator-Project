## Описание проекта

Приложение представляет собой сервис для перевода текста, использующий API Google Translate. Оно также сохраняет запросы на перевод в базу данных PostgreSQL.

## Требования

* Java 17
* Maven
* Spring Boot 3
* PostgreSQL
* Docker

## Структура проекта

```
demo
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── demo
│   │   │               ├── DemoApplication.java
│   │   │               ├── config
│   │   │               │   ├── GoogleConfig.java
│   │   │               │   └── TextConfig.java
│   │   │               ├── controller
│   │   │               │   ├── TextController.java
│   │   │               ├── dto
│   │   │               │   ├── TextRequest.java
│   │   │               │   └── TextResponse.java
│   │   │               ├── exception
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   ├── LanguageNotFoundException.java
│   │   │               │   └── TranslationResourceAccessException.java
│   │   │               ├── repository
│   │   │               │   └── TranslationRepository.java
│   │   │               └── service
│   │   │                   ├── TextService.java
│   │   │                   └── impl
│   │   │                       └── TextServiceImplTest.java
│   │   └── resources
│   │       └── application.yml
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── demo
│                       ├── DemoApplicationTests.java
│                       └── service
│                       │   └── TextServiceImplTest.java
│                       └── controller
│                           └── TextControllerTest.java
└── pom.xml

```

## Запуск

#### 1) Скопируйте приложение:
```
git clone https://github.com/KonovalovaAN/t-bank-lab.git
```
#### 2) Перейдите в корневую папку проекта.
#### 3) Создайте файл .jar:

```
mvnw.cmd clean package -DskipTests=true
```
#### 4) Создайте Docker image:

```
docker build ./ -t webapi
```
#### 5) Запустите приложение:

```
docker-compose up -d
```
Приложение начнет работать по адресу [http://localhost:8080](http://localhost:8080) и уже готово к использованию.
Для отслеживания изменений в базе данных перейдите по адресу [http://localhost:8081](http://localhost:8081). Вы увидите интерфейс Adminer. Используйте следующие данные для входа:

* System: PostgreSQL
* Server: db
* Username: user
* Password: password
* Database: translations

#### 6) Остановка и удаление контейнеров:
Для остановки и удаления контейнеров воспользуйтесь командой:

```
docker-compose down 
```
  
При желании данные для входа можно изменить. Для этого в файле application.yaml измените необходимые параметры:

```
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/translations
    username: user (Ваше имя пользователя)
    password: password (Ваш пароль)
    driver-class-name: org.postgresql.Driver
```

Также ваше имя пользователя и пароль следует изменить в файле docker-compose.yaml

```
#######################################################################

services:
  db:
    image: postgres:16
    container_name: postgres-db
    environment:
      POSTGRES_USER: user (Ваше имя пользователя)
      POSTGRES_PASSWORD: password (Ваш пароль)
      POSTGRES_DB: translations

#######################################################################

  app:
    build: .
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/translations
      SPRING_DATASOURCE_USERNAME: user (Ваше имя пользователя)
      SPRING_DATASOURCE_PASSWORD: password (Ваш пароль)

#######################################################################
```
## Использование

1. Откройте браузер и перейдите по адресу [http://localhost:8080](http://localhost:8080).
   
2. В интерфейсе вы увидите следующее:

    - Два выпадающих списка для выбора исходного языка (From) и целевого языка (To).
    - Поле для ввода текста, который необходимо перевести (Enter text to translate).
    - Поле для отображения переведенного текста (Your translated text).
    - Кнопка "Translate" для запуска процесса перевода.

3. Взаимодействие с интерфейсом:

    - Выберите исходный язык в выпадающем списке "From".
    - Выберите целевой язык в выпадающем списке "To".
    - Введите текст, который вы хотите перевести, в поле "Enter text to translate".
    - Нажмите кнопку "Translate".
    - Переведенный текст отобразится в поле "Your translated text".

Для отслеживания изменений в базе данных перейдите по адресу [http://localhost:8081](http://localhost:8081). Вы увидите интерфейс Adminer. Используйте следующие данные для входа:

* System: PostgreSQL
* Server: db
* Username: user (либо Ваше изменённое имя пользователя) 
* Password: password (либо Ваш изменённый пароль)
* Database: translations
