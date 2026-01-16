# syntax=docker/dockerfile:1

FROM gradle:8.14-jdk11@sha256:03864bfa7750eb95b1e998bb57c367cf3301255636c50e25fd8ad6298a5ea624 AS build
WORKDIR /home/gradle/compile

COPY --chown=gradle:gradle build.gradle settings.gradle /home/gradle/compile/
RUN gradle build check --no-daemon

COPY --chown=gradle:gradle src/ /home/gradle/compile/src/
RUN gradle build check --no-daemon


FROM eclipse-temurin:11.0.29_7-jre-noble@sha256:bc7695feae7b49288bedcfe4b068613cafcf0beacc05818de7c4955bc611a998
COPY --from=build /home/gradle/compile/build/libs/*all.jar /app/ShlinkDiscordBot.jar

ENTRYPOINT ["java", "-jar", "/app/ShlinkDiscordBot.jar"]
