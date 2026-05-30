# ms-plataforma-online

Microservicio de inscripción de cursos (Cloud Native, Duoc CDY2204).

- **Semana 1:** CRUD de cursos, inscripciones y estudiantes (Spring Boot + JPA + Oracle).
- **Semana 2:** generación del resumen de inscripción en **PDF** y almacenamiento del archivo en **AWS S3**.

Stack: Spring Boot 3.3.7, Java 17, Maven, JPA/Oracle, OpenPDF, AWS SDK v2, springdoc/Swagger.
Paquete base: `com.plataforma` · groupId `com.plataforma` · artifactId `ms-plataforma-online`.

## Compilar y probar (Docker JDK17)

El host puede tener un JDK incompatible con Spring Boot 3.3.x. Usa Docker con JDK17:

```bash
# Compilar
docker run --rm -v "$PWD":/app -v "$HOME/.m2":/root/.m2 -w /app \
  maven:3.9-eclipse-temurin-17 ./mvnw -q -DskipTests compile

# Tests (usan H2 en memoria y S3 mockeado: NO requieren Oracle ni AWS)
docker run --rm -v "$PWD":/app -v "$HOME/.m2":/root/.m2 -w /app \
  maven:3.9-eclipse-temurin-17 ./mvnw -q test
```

## Variables de entorno (AWS Learner Lab)

Las credenciales del AWS Academy Learner Lab son **temporales** y requieren `session token`.
Cópialas desde *AWS Details → AWS CLI* y expórtalas (ver `.env.example`):

| Variable                 | Descripción                              |
|--------------------------|------------------------------------------|
| `AWS_REGION`             | Región del bucket (ej. `us-east-1`)      |
| `AWS_S3_BUCKET`          | Nombre del bucket S3                      |
| `AWS_ACCESS_KEY_ID`      | Access key temporal                      |
| `AWS_SECRET_ACCESS_KEY`  | Secret key temporal                      |
| `AWS_SESSION_TOKEN`      | Session token temporal                   |

Las credenciales **nunca** se hardcodean; se inyectan por entorno y el `S3Client` se construye
como `@Bean` con `StaticCredentialsProvider` + `AwsSessionCredentials`.

## Endpoints

### Cursos (`/api/cursos`)
- `GET    /api/cursos` — listar cursos
- `GET    /api/cursos/{id}` — obtener curso
- `POST   /api/cursos` — crear curso

### Estudiantes (`/api/students`)
- `GET    /api/students` — listar estudiantes
- `GET    /api/students/{id}` — obtener estudiante
- `POST   /api/students` — crear estudiante
- `PUT    /api/students/{id}` — actualizar estudiante
- `DELETE /api/students/{id}` — borrar estudiante

### Inscripciones (`/api/inscripciones`)
- `POST   /api/inscripciones` — inscribe al estudiante en cursos y crea un **Resumen** persistido.
  La respuesta incluye `numero` (número de resumen) para consultarlo y subirlo a S3.

### Resúmenes y archivo PDF (`/api/resumenes`) — Semana 2
- `GET    /api/resumenes/{numero}` — consultar el resumen
- `GET    /api/resumenes/{numero}/archivo` — descargar el **PDF** (generado al vuelo)
- `POST   /api/resumenes/{numero}/s3` — generar el PDF y **subirlo** a S3 en `s3://{bucket}/{numero}/resumen-{numero}.pdf`
- `GET    /api/resumenes/{numero}/s3` — **descargar** el PDF desde S3
- `PUT    /api/resumenes/{numero}/s3` — **modificar** (re-generar y re-subir) el PDF en S3
- `DELETE /api/resumenes/{numero}/s3` — **borrar** el objeto de S3

Cada resumen se guarda en una carpeta del bucket cuyo nombre es el **número del resumen**.

Swagger UI disponible en `/swagger-ui.html`.

## Flujo de prueba rápido (S2)

```bash
# 1) Inscribir -> devuelve "numero"
curl -s -X POST localhost:8080/api/inscripciones \
  -H 'Content-Type: application/json' \
  -d '{"estudianteNombre":"Diego","cursoIds":[1,2]}'

# 2) Descargar el PDF localmente
curl -s -o resumen.pdf localhost:8080/api/resumenes/1/archivo

# 3) Subir el PDF a S3
curl -s -X POST localhost:8080/api/resumenes/1/s3

# 4) Descargar desde S3 / modificar / borrar
curl -s -o desde_s3.pdf localhost:8080/api/resumenes/1/s3
curl -s -X PUT    localhost:8080/api/resumenes/1/s3
curl -s -X DELETE localhost:8080/api/resumenes/1/s3
```
