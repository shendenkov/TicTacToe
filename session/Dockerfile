FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw -B \
    -Dmaven.artifact.threads=2 \
    -Dmaven.wagon.http.retryHandler.count=5 \
    -Dmaven.wagon.http.timeout=60000 \
    -Dmaven.wagon.http.readTimeout=60000 \
    dependency:go-offline

COPY src src

RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw -B \
    -Dmaven.artifact.threads=2 \
    -Dmaven.wagon.http.retryHandler.count=5 \
    -Dmaven.wagon.http.timeout=60000 \
    -Dmaven.wagon.http.readTimeout=60000 \
    clean package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/session-1.0.1.jar TicTacToeGameSession.jar
ENTRYPOINT ["java","-jar","TicTacToeGameSession.jar"]