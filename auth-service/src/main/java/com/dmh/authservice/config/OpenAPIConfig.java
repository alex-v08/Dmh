package com.dmh.authservice.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI authServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info ()
                        .title("Auth Service API")
                        .description("Authentication and Authorization Service for Digital Money House")
                        .version("1.0.0")
                        .contact(new Contact ()
                                .name("Digital Money House Team")
                                .email("support@digitalmoney.house")
                                .url("https://www.digitalmoney.house"))
                        .license(new License ()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation ()
                        .description("Auth Service Wiki Documentation")
                        .url("https://wiki.digitalmoney.house/auth"))
                .addSecurityItem(new SecurityRequirement ().addList("bearer-jwt"))
                .components(new Components ()
                        .addSecuritySchemes("bearer-jwt",
                                new SecurityScheme ()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token authentication")));
    }
}