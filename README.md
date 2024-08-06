# T-Bank Lab - Translator

## Описание проекта

Данный проект представляет собой выполнение лабораторной работы по Java для курса "Java-разработчик, осень 2024" от T-Банка. Приложение представляет собой сервис для перевода текста, использующий API Google Translate. Оно также сохраняет запросы на перевод в базу данных PostgreSQL.

## Требования

* Java 17
* Maven
* Spring Boot 3
* PostgreSQL
* Docker

## Запуск

#### 1) Скопируйте приложение:
```
git clone https://github.com/KonovalovaAN/test-repos.git
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
Приложение начнет работать по адресу http://localhost:8080
Для отслеживания изменений в базе данных перейдите по адресу http://localhost:8081. Вы увидите интерфейс Adminer. Используйте следующие данные для входа:

* System: PostgreSQL
* Server: db
* Username: user
* Password: password
* Database: translations
  
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

