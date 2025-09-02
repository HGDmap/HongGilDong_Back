# Build stage
FROM amazoncorretto:17 AS builder
WORKDIR /app

# gradle wrapper 및 설정 복사
COPY gradlew build.gradle settings.gradle /app/
COPY gradle /app/gradle

RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

# 실제 소스 복사
COPY . .

# jar 빌드
RUN ./gradlew clean build -x test --no-daemon

# Run stage
FROM amazoncorretto:17
WORKDIR /app
EXPOSE 8080

# 빌드 산출물 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
