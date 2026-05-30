FROM eclipse-temurin:17-jdk AS buildstage

RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY pom.xml .
COPY src /app/src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre

COPY --from=buildstage /app/target/ms-plataforma-online-0.0.1-SNAPSHOT.jar /app/ms-plataforma-online.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar","/app/ms-plataforma-online.jar" ]
