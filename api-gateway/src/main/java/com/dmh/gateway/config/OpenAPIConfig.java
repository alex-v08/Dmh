package com.dmh.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server()
                .url("http://localhost:" + serverPort)
                .description("Gateway Server");

        return new OpenAPI()
                .info(new Info()
                        .title("Digital Money House API Gateway")
                        .version("1.0")
                        .description("API Gateway para Digital Money House Financial Services")
                        .contact(new Contact()
                                .name("Digital Money House Team")
                                .email("support@digitalmoney.house")
                                .url("https://digitalmoney.house"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(localServer));
    }

    @Bean
    public List<GroupedOpenApi> apis() {
        return List.of(
                GroupedOpenApi.builder()
                        .group("gateway")
                        .pathsToMatch("/**")
                        .build(),
                GroupedOpenApi.builder()
                        .group("auth")
                        .pathsToMatch("/api/auth/**")
                        .build(),
                GroupedOpenApi.builder()
                        .group("users")
                        .pathsToMatch("/api/users/**")
                        .build(),
                GroupedOpenApi.builder()
                        .group("accounts")
                        .pathsToMatch("/api/accounts/**")
                        .build(),
                GroupedOpenApi.builder()
                        .group("transactions")
                        .pathsToMatch("/api/transactions/**")
                        .build(),
                GroupedOpenApi.builder()
                        .group("cards")
                        .pathsToMatch("/api/cards/**")
                        .build()
        );
    }
}