package com.dmh.gateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth Service Routes
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("lb://AUTH-SERVICE"))

                // User Service Routes
                .route("user-service", r -> r.path("/api/users/**")
                        .uri("lb://USER-SERVICE"))

                // Account Service Routes
                .route("account-service", r -> r.path("/api/accounts/**")
                        .uri("lb://ACCOUNT-SERVICE"))

                // Transaction Service Routes
                .route("transaction-service", r -> r.path("/api/transactions/**")
                        .uri("lb://TRANSACTION-SERVICE"))

                // Card Service Routes
                .route("card-service", r -> r.path("/api/cards/**")
                        .uri("lb://CARD-SERVICE"))

                // Swagger UI route
                .route("swagger-ui", r -> r.path("/swagger-ui/**")
                        .uri("http://localhost:8080"))

                // OpenAPI docs route
                .route("api-docs", r -> r.path("/v3/api-docs/**")
                        .uri("http://localhost:8080"))

                .build();
    }
}