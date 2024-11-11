package com.dmh.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Swagger UI route
                .route("swagger-ui", r -> r.path("/swagger-ui/**")
                        .uri("http://localhost:8080"))
                // OpenAPI specs route
                .route("api-docs", r -> r.path("/v3/api-docs/**")
                        .uri("http://localhost:8080"))

                // Existing service routes
                .route("user-service", r -> r.path("/api/users/**")
                        .uri("lb://USER-SERVICE"))
                .route("account-service", r -> r.path("/api/accounts/**")
                        .uri("lb://ACCOUNT-SERVICE"))
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("lb://AUTH-SERVICE"))
                .route("transaction-service", r -> r.path("/api/transactions/**")
                        .uri("lb://TRANSACTION-SERVICE"))
                .route("card-service", r -> r.path("/api/cards/**")
                        .uri("lb://CARD-SERVICE"))
                .build();
    }
}