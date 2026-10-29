# syntax=docker/dockerfile:1@sha256:4edf897a3ffa55b89f906fc8cc78afdb3f1834cc9c7083565e611a8a7d5fe99e

FROM gradle:8.14-jdk11@sha256:bdd652d6a71711400950d214a3eaf701ff42997e564933265633dd81af000ba7 AS build
WORKDIR /home/gradle/compile

COPY --chown=gradle:gradle build.gradle settings.gradle /home/gradle/compile/
RUN gradle build check --no-daemon

COPY --chown=gradle:gradle src/ /home/gradle/compile/src/
RUN gradle build check --no-daemon


FROM eclipse-temurin:25.0.5_8-jre-noble@sha256:39d8a64f84d4c0aab4639757e1502264da350be831df4bb25d182d6a514113c2
COPY --from=build /home/gradle/compile/build/libs/*all.jar /app/ShlinkDiscordBot.jar

ENTRYPOINT ["java", "-jar", "/app/ShlinkDiscordBot.jar"]
