package com.dmh.cardservice.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("msvc-cards")
                        .description("Gestionará las operaciones relacionadas con las tarjetas de crédito y débito asociadas a las cuentas de los usuarios")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Facundo Ledezma")
                                .email("facled@yahoo.com.ar")
                                .url("https://www.digitalhouse.com"))
                        .license(new License()
                                .name("Licencia API-Fac")
                                .url("https://www.digitalhouse.com")));
    }
}
