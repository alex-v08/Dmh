package com.dmh.gateway.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final ReactiveJwtDecoder jwtDecoder;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .filter(auth -> auth instanceof JwtAuthenticationToken)
                .cast(JwtAuthenticationToken.class)
                .flatMap(this::validateToken)
                .onErrorMap(e -> {
                    log.error("Authentication error", e);
                    return new InvalidBearerTokenException("Invalid JWT token", e);
                });
    }

    private Mono<Authentication> validateToken(JwtAuthenticationToken auth) {
        String token = auth.getToken().getTokenValue();
        return jwtDecoder.decode(token)
                .map(this::convertToAuthenticationToken)
                .onErrorMap(e -> {
                    log.error("Token validation error", e);
                    return new InvalidBearerTokenException("Token validation failed", e);
                });
    }

    private Authentication convertToAuthenticationToken(Jwt jwt) {
        Collection<SimpleGrantedAuthority> authorities = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, authorities);
    }

    @SuppressWarnings("unchecked")
    private Collection<SimpleGrantedAuthority> extractAuthorities(Jwt jwt) {
        Stream<String> resourceRoles = extractResourceRoles(jwt);
        Stream<String> realmRoles = extractRealmRoles(jwt);

        return Stream.concat(resourceRoles, realmRoles)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private Stream<String> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) {
            return Stream.empty();
        }

        Map<String, Object> resource = (Map<String, Object>) resourceAccess.get("gateway-client");
        if (resource == null) {
            return Stream.empty();
        }

        Collection<String> resourceRoles = (Collection<String>) resource.get("roles");
        return resourceRoles != null ? resourceRoles.stream() : Stream.empty();
    }

    @SuppressWarnings("unchecked")
    private Stream<String> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null) {
            return Stream.empty();
        }

        Collection<String> realmRoles = (Collection<String>) realmAccess.get("roles");
        return realmRoles != null ? realmRoles.stream() : Stream.empty();
    }
}