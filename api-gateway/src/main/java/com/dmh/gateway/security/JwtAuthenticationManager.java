package com.dmh.gateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final ReactiveJwtDecoder jwtDecoder;

    public JwtAuthenticationManager(
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) {
        this.jwtDecoder = ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .map(Authentication::getCredentials)
                .cast(String.class)
                .flatMap(jwtDecoder::decode)
                .map(jwt -> {
                    List<String> realmRoles = jwt.getClaimAsStringList("realm_access.roles");
                    if (realmRoles == null) {
                        realmRoles = List.of();
                    }

                    List<SimpleGrantedAuthority> authorities = realmRoles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                            .collect(Collectors.toList());

                    String username = jwt.getClaimAsString("preferred_username");

                    return new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            authorities
                    );
                });
    }
}
