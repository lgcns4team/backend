# AWS 환경이면 amazoncorretto가 많이 사용되기도 함
# alpine 이미지 크기가 작아 장점이지만 패키지 부족이나 라이브러리 호환 문제가 생길 수도 있다.
# CI 작업에서 JDK를 쓰고 실 서버에서 JRE로 실행만 시킬 수 있다.
# FROM eclipse-temurin:17-jdk-alpine
FROM eclipse-temurin:17-jdk

WORKDIR /app

#ARG JAR_FILE=build/libs/*.jar
#COPY ${JAR_FILE} app.jar
COPY build/libs/app.jar /app/app.jar

# RUN은 이미지를 만들 때만 실행 됨 (한 번)
RUN chmod +x /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]