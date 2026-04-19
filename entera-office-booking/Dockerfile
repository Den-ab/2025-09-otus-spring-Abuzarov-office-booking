FROM bellsoft/liberica-openjdk-alpine:21
ARG VERSION

# Копируем собранный JAR-файл из стадии сборки
COPY ./target/entera-office-booking-*.jar /app/entera-office-booking.jar


# Запуск приложения (запуск сконфигурировать в docker-compose с различными параметрами запуска.)
ENTRYPOINT ["sh", "-c", "java -jar /app/entera-office-booking.jar"]
