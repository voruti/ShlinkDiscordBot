# syntax=docker/dockerfile:1@sha256:ecfaec9ed6d810b56388c508f4121597bfbba70d41a6dfeee4d8cad5f295fc32

FROM gradle:8.14-jdk11@sha256:42e9e989cd7b83bf89d590ebe356bed75a5ae5fc26bfe933aae011907f6fee2c AS build
WORKDIR /home/gradle/compile

COPY --chown=gradle:gradle build.gradle settings.gradle /home/gradle/compile/
RUN gradle build check --no-daemon

COPY --chown=gradle:gradle src/ /home/gradle/compile/src/
RUN gradle build check --no-daemon


FROM eclipse-temurin:11.0.32_9-jre-noble@sha256:bbdae0232a87a7b63dec7594840427db46caa2ee475f6aaeb4e000950aa7f2b8
COPY --from=build /home/gradle/compile/build/libs/*all.jar /app/ShlinkDiscordBot.jar

ENTRYPOINT ["java", "-jar", "/app/ShlinkDiscordBot.jar"]
