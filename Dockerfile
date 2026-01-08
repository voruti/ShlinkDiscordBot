FROM gradle:8.14-jdk11@sha256:4520e9eecb2d47b383c4da60b797397707a61cac3b78162fb2ca9219768cea00 AS build
WORKDIR /home/gradle/src

COPY --chown=gradle:gradle build.gradle settings.gradle /home/gradle/src/
RUN gradle build check --no-daemon

COPY --chown=gradle:gradle src/ /home/gradle/src/src/
RUN gradle build check --no-daemon


FROM eclipse-temurin:11.0.29_7-jre-noble@sha256:f0d58cecb9da530f228e3eb4cb2da222eba9255117d8fbf8cbd2ff7b7575f3b4
COPY --from=build /home/gradle/src/build/libs/*all.jar /app/ShlinkDiscordBot.jar

ENTRYPOINT ["java", "-jar", "/app/ShlinkDiscordBot.jar"]
