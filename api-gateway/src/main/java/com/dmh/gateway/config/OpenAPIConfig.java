package com.dmh.gateway.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;
import java.util.List;
@Configuration
public class OpenAPIConfig {

    @Primary  // Agregamos @Primary para evitar conflictos
    @Bean
    public OpenAPI gatewayOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Digital Money House API")
                        .description("Central API Gateway for Digital Money House Microservices")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Digital Money House Team")
                                .email("support@digitalmoney.house")
                                .url("https://www.digitalmoney.house"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Digital Money House Documentation")
                        .url("https://wiki.digitalmoney.house"))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"))
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8080").description("Gateway Server"),
                        new Server().url("http://localhost:8083").description("Auth Service"),
                        new Server().url("http://localhost:8081").description("User Service"),
                        new Server().url("http://localhost:8082").description("Account Service"),
                        new Server().url("http://localhost:8085").description("Card Service"),
                        new Server().url("http://localhost:8097").description("Transaction Service")
                ));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/public/**")
                .build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch("/api/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi usersApi() {
        return GroupedOpenApi.builder()
                .group("users")
                .pathsToMatch("/api/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi accountsApi() {
        return GroupedOpenApi.builder()
                .group("accounts")
                .pathsToMatch("/api/accounts/**")
                .build();
    }

    @Bean
    public GroupedOpenApi transactionsApi() {
        return GroupedOpenApi.builder()
                .group("transactions")
                .pathsToMatch("/api/transactions/**")
                .build();
    }

    @Bean
    public GroupedOpenApi cardsApi() {
        return GroupedOpenApi.builder()
                .group("cards")
                .pathsToMatch("/api/cards/**")
                .build();
    }
}