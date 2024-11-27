package com.dmh.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.ExternalDocumentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI apiGatewayOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Digital Money House - API Gateway")
                        .description("Gateway API para Digital Money House - Punto de entrada centralizado para todos los microservicios")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Digital Money House Team")
                                .email("support@digitalmoney.house")
                                .url("https://www.digitalmoney.house"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Digital Money House Wiki Documentation")
                        .url("https://wiki.digitalmoney.house"))
                .servers(getServers())
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Please enter JWT token"))
                        .addSecuritySchemes("oauth2", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .description("OAuth2 authentication")
                                .flows(configureOAuthFlows())))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"))
                .addSecurityItem(new SecurityRequirement().addList("oauth2"));
    }

    private List<Server> getServers() {
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Local Development Server");

        Server devServer = new Server()
                .url("https://dev-api.digitalmoney.house")
                .description("Development Server");

        Server prodServer = new Server()
                .url("https://api.digitalmoney.house")
                .description("Production Server");

        return Arrays.asList(localServer, devServer, prodServer);
    }

    private OAuthFlows configureOAuthFlows() {
        return new OAuthFlows()
                .authorizationCode(new OAuthFlow()
                        .authorizationUrl("http://localhost:9092/realms/dmh/protocol/openid-connect/auth")
                        .tokenUrl("http://localhost:9092/realms/dmh/protocol/openid-connect/token")
                        .refreshUrl("http://localhost:9092/realms/dmh/protocol/openid-connect/token"));
    }
}