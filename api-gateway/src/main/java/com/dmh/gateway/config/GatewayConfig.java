package com.dmh.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth Service Routes
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("authCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/auth"))
                                .rewritePath("/api/(?<segment>.*)", "/$\\{segment}")
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setMethods(HttpMethod.GET, HttpMethod.POST)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true))
                        )
                        .uri("lb://AUTH-SERVICE"))

                // Auth Service API Docs
                .route("auth-docs", r -> r
                        .path("/v3/api-docs/auth-service")
                        .filters(f -> f.rewritePath("/v3/api-docs/auth-service", "/v3/api-docs"))
                        .uri("lb://AUTH-SERVICE"))

                // User Service Routes
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("userCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/users"))
                                .rewritePath("/api/(?<segment>.*)", "/$\\{segment}")
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true))
                        )
                        .uri("lb://USER-SERVICE"))

                // Account Service Routes
                .route("account-service", r -> r
                        .path("/api/accounts/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("accountCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/accounts"))
                                .rewritePath("/api/(?<segment>.*)", "/$\\{segment}")
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true))
                        )
                        .uri("lb://ACCOUNT-SERVICE"))

                // Transaction Service Routes
                .route("transaction-service", r -> r
                        .path("/api/transactions/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("transactionCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/transactions"))
                                .rewritePath("/api/(?<segment>.*)", "/$\\{segment}")
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setMethods(HttpMethod.GET, HttpMethod.POST)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true))
                        )
                        .uri("lb://TRANSACTION-SERVICE"))

                // Card Service Routes
                .route("card-service", r -> r
                        .path("/api/cards/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("cardCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/cards"))
                                .rewritePath("/api/(?<segment>.*)", "/$\\{segment}")
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true))
                        )
                        .uri("lb://CARD-SERVICE"))
                .build();
    }

    @Bean
    KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}