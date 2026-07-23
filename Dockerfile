# syntax=docker/dockerfile:1@sha256:87999aa3d42bdc6bea60565083ee17e86d1f3339802f543c0d03998580f9cb89

FROM gradle:8.14-jdk11@sha256:1b8aedd98fe6ee29dd72cd5270dbbcf9641e39ed64e574ff3078a42952fd5ac4 AS build
WORKDIR /home/gradle/compile

COPY --chown=gradle:gradle build.gradle settings.gradle /home/gradle/compile/
RUN gradle build check --no-daemon

COPY --chown=gradle:gradle src/ /home/gradle/compile/src/
RUN gradle build check --no-daemon


FROM eclipse-temurin:11.0.31_11-jre-noble@sha256:213ce49ed6fe8bf7f3badaba2d37215389488a36b6fda2f72211aa4cf76cbf44
COPY --from=build /home/gradle/compile/build/libs/*all.jar /app/ShlinkDiscordBot.jar

ENTRYPOINT ["java", "-jar", "/app/ShlinkDiscordBot.jar"]
