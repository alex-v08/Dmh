package com.dmh.cardservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OpenAPIConfig {

    @Bean
    @Primary  // Esta anotación le dice a Spring que prefiera este bean cuando haya ambigüedad
    public OpenAPI cardServiceOpenAPI() {
        // Construyendo una especificación OpenAPI completa
        return new OpenAPI()
                .info(new Info()
                        .title("API de Servicio de Tarjetas")
                        .description("Digital Money House - Documentación de la API REST del Servicio de Tarjetas")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo DMH")
                                .email("soporte@dmh.com")
                                .url("https://www.dmh.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .addServersItem(new Server()
                        .url("http://localhost:8085")
                        .description("Servidor de Desarrollo Local"));
    }
}