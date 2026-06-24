# syntax=docker/dockerfile:1

FROM gradle:8.14-jdk11@sha256:1b8aedd98fe6ee29dd72cd5270dbbcf9641e39ed64e574ff3078a42952fd5ac4 AS build
WORKDIR /home/gradle/compile

COPY --chown=gradle:gradle build.gradle settings.gradle /home/gradle/compile/
RUN gradle build check --no-daemon

COPY --chown=gradle:gradle src/ /home/gradle/compile/src/
RUN gradle build check --no-daemon


FROM eclipse-temurin:25.0.3_9-jre-noble@sha256:f9bd8815e73632c22985ebb133ec49b9fc4ad5ffe0657594ac02748ad0431ab7
COPY --from=build /home/gradle/compile/build/libs/*all.jar /app/ShlinkDiscordBot.jar

ENTRYPOINT ["java", "-jar", "/app/ShlinkDiscordBot.jar"]
