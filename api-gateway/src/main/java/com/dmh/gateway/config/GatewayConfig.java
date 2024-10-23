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
                .route("user-service", r -> r.path("/api/users/**")
                        .uri("lb://USER-SERVICE"))
                .route("account-service", r -> r.path("/api/accounts/**")
                        .uri("lb://ACCOUNT-SERVICE"))
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("lb://AUTH-SERVICE"))
                .route("transaction-service", r -> r.path("/api/transactions/**")
                        .uri("lb://TRANSACTION-SERVICE"))
                .route("generate-cvu-service", r -> r.path("/api/cvu/**")
                        .uri("lb://GENERATE-CVU"))
                .route("generate-alias-service", r -> r.path("/api/alias/**")
                        .uri("lb://GENERATE-ALIAS"))
                .build();
    }
}