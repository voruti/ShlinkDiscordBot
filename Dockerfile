# syntax=docker/dockerfile:1@sha256:2780b5c3bab67f1f76c781860de469442999ed1a0d7992a5efdf2cffc0e3d769

FROM gradle:8.14-jdk11@sha256:10dd4bd3785808d0be201684e0a5e8ec4d1ac58dc2ce45def791ce10eb44dd9a AS build
WORKDIR /home/gradle/compile

COPY --chown=gradle:gradle build.gradle settings.gradle /home/gradle/compile/
RUN gradle build check --no-daemon

COPY --chown=gradle:gradle src/ /home/gradle/compile/src/
RUN gradle build check --no-daemon


FROM eclipse-temurin:11.0.30_7-jre-noble@sha256:b1e9d673735829475f372ccbaf004ba35c9ae62634e2e47255ccb9fafdb5cc52
COPY --from=build /home/gradle/compile/build/libs/*all.jar /app/ShlinkDiscordBot.jar

ENTRYPOINT ["java", "-jar", "/app/ShlinkDiscordBot.jar"]
