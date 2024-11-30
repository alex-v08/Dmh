/*
package com.dmh.accountservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI accountServiceOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8082");
        localServer.setDescription("Local Environment");

        Info info = new Info()
                .title("Account Service API")
                .description("Account Management API Documentation")
                .version("1.0")
                .license(new License()
                        .name("Apache 2.0")
                        .url("http://springdoc.org"));

        return new OpenAPI()
                .info(info)
                .servers(Arrays.asList(localServer));
    }
}*/
