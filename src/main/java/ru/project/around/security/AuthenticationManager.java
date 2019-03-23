package ru.project.around.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.project.around.util.jwt.JwtTokenUtils;

import java.util.Collections;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenUtils jwtTokenUtils;

    @Autowired
    public AuthenticationManager(final JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    public Mono<Authentication> authenticate(final Authentication authentication) {
        final String accessToken = authentication.getCredentials().toString();
        if (jwtTokenUtils.isTokenValid(accessToken)) {
            return Mono.just(new UsernamePasswordAuthenticationToken(
                    jwtTokenUtils.getUserEmail(accessToken),
                    null,
                    Collections.emptyList()
            ));
        }
        return Mono.empty();
    }
}
