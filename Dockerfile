FROM noelware/openjdk:latest AS builder

WORKDIR /
COPY . .
RUN chmod +x gradlew
RUN ./gradlew build

FROM noelware/openjdk:latest

WORKDIR /app/sushi
COPY --from=builder /build/libs/sushi.jar /app/sushi/sushi.jar

CMD ["java", "-jar", "sushi.jar"]
