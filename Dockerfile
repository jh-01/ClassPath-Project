# 1단계: Gradle 빌드 (테스트 제외)
FROM gradle:8.6-jdk17 AS build

WORKDIR /app
COPY . .
RUN gradle build -x test --no-daemon

# 2단계: 실행용 경량 이미지
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=build /app/build/libs/*-SNAPSHOT.jar ./app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=default", "/app/app.jar"]
