# Используем официальный образ OpenJDK как базовый образ
FROM openjdk:20-jdk-slim

# Указываем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем jar-файл приложения в контейнер
COPY target/translator-0.0.1-SNAPSHOT.jar /app/translator-0.0.1-SNAPSHOT.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "/app/translator-0.0.1-SNAPSHOT.jar"]
