FROM eclipse-temurin:22-jdk AS buildstage
 
RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY pom.xml .
COPY src /app/src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:22-jdk

COPY --from=buildstage /app/target/plataforma-inscripciones-0.0.1-SNAPSHOT.jar /app/plataforma-inscripciones.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar","/app/plataforma-inscripciones.jar" ]
