package com.plataforma.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Plataforma Inscripciones API")
                        .version("2.0")
                        .description("API para inscripción de cursos - Cloud Native. "
                                + "S1: cursos/inscripciones. S2: resumen en PDF y almacenamiento en AWS S3."));
    }
}
