FROM gradle:6.9-jdk11-openj9 AS build
WORKDIR /home/gradle/src

COPY --chown=gradle:gradle build.gradle settings.gradle /home/gradle/src/
RUN gradle build check --no-daemon

COPY --chown=gradle:gradle src/ /home/gradle/src/src/
RUN gradle build check --no-daemon


FROM openjdk:11.0.16-jre-slim
COPY --from=build /home/gradle/src/build/libs/*all.jar /app/ShlinkDiscordBot.jar

ENTRYPOINT ["java", "-jar", "/app/ShlinkDiscordBot.jar"]
