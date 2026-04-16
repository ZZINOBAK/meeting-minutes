# 1단계: 빌드 (Gradle로 .jar 파일 만들기)
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .

# 권한 부여 후 빌드 진행 (테스트는 스킵해서 속도 향상)
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar -x test

# 2단계: 실행 (가벼운 JRE 환경에서 실행)
FROM eclipse-temurin:21-jre
WORKDIR /app

# 빌드 단계에서 만든 jar 파일만 쏙 가져오기
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]