FROM gradle:8.11-jdk11 AS build
WORKDIR /home/gradle/src

COPY --chown=gradle:gradle build.gradle settings.gradle /home/gradle/src/
RUN gradle build check --no-daemon

COPY --chown=gradle:gradle src/ /home/gradle/src/src/
RUN gradle build check --no-daemon


FROM eclipse-temurin:11.0.25_9-jre-noble
COPY --from=build /home/gradle/src/build/libs/*all.jar /app/ShlinkDiscordBot.jar

ENTRYPOINT ["java", "-jar", "/app/ShlinkDiscordBot.jar"]
