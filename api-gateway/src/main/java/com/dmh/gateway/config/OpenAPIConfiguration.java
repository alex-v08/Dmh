package com.dmh.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
@Configuration
public class OpenAPIConfiguration {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server()
                .url("http://localhost:" + serverPort)
                .description("Gateway Server");

        Contact contact = new Contact()
                .name("Digital Money House Team")
                .email("support@digitalmoney.house")
                .url("https://digitalmoney.house");

        License license = new License()
                .name("Apache 2.0")
                .url("http://www.apache.org/licenses/LICENSE-2.0.html");

        return new OpenAPI()
                .info(new Info()
                        .title("Digital Money House API Gateway")
                        .version("1.0")
                        .description("API Gateway for Digital Money House Financial Services")
                        .contact(contact)
                        .license(license))
                .servers(List.of(localServer))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token authentication")));
    }

    @Bean
    public List<GroupedOpenApi> apis() {
        return List.of(
                GroupedOpenApi.builder()
                        .group("gateway-api")
                        .pathsToMatch("/**")
                        .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                                .title("Gateway API")
                                .version("1.0")))
                        .build(),
                GroupedOpenApi.builder()
                        .group("auth-service")
                        .pathsToMatch("/api/auth/**", "/auth-service/v3/api-docs/**")
                        .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                                .title("Authentication Service API")
                                .version("1.0")))
                        .build(),
                GroupedOpenApi.builder()
                        .group("user-service")
                        .pathsToMatch("/api/users/**", "/user-service/v3/api-docs/**")
                        .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                                .title("User Management Service API")
                                .version("1.0")))
                        .build(),
                GroupedOpenApi.builder()
                        .group("account-service")
                        .pathsToMatch("/api/accounts/**", "/account-service/v3/api-docs/**")
                        .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                                .title("Account Management Service API")
                                .version("1.0")))
                        .build(),
                GroupedOpenApi.builder()
                        .group("transaction-service")
                        .pathsToMatch("/api/transactions/**", "/transaction-service/v3/api-docs/**")
                        .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                                .title("Transaction Service API")
                                .version("1.0")))
                        .build(),
                GroupedOpenApi.builder()
                        .group("card-service")
                        .pathsToMatch("/api/cards/**", "/card-service/v3/api-docs/**")
                        .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                                .title("Card Management Service API")
                                .version("1.0")))
                        .build()
        );
    }

    @Bean
    public GroupedOpenApi actuatorApi() {
        return GroupedOpenApi.builder()
                .group("actuator")
                .pathsToMatch("/actuator/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("Actuator API")
                        .version("1.0")
                        .description("Spring Boot Actuator Web API")))
                .build();
    }
}