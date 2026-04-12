# syntax=docker/dockerfile:1

FROM gradle:8.14-jdk11@sha256:10dd4bd3785808d0be201684e0a5e8ec4d1ac58dc2ce45def791ce10eb44dd9a AS build
WORKDIR /home/gradle/compile

COPY --chown=gradle:gradle build.gradle settings.gradle /home/gradle/compile/
RUN gradle build check --no-daemon

COPY --chown=gradle:gradle src/ /home/gradle/compile/src/
RUN gradle build check --no-daemon


FROM eclipse-temurin:25.0.2_10-jre-noble@sha256:aa8759eb18c5fcab069c62558a6f7e0b26359c29cc889a29ed7e5bbaed6a9ce5
COPY --from=build /home/gradle/compile/build/libs/*all.jar /app/ShlinkDiscordBot.jar

ENTRYPOINT ["java", "-jar", "/app/ShlinkDiscordBot.jar"]
