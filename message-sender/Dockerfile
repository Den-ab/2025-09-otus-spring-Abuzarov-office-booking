FROM bellsoft/liberica-openjdk-alpine:17
ARG VERSION

# Копируем собранный JAR-файл из стадии сборки
COPY ./target/message-sender-*.jar /app/message-sender.jar


# Запуск приложения (запуск сконфигурировать в docker-compose с различными параметрами запуска.)
ENTRYPOINT ["sh", "-c", "java -jar /app/message-sender.jar"]
