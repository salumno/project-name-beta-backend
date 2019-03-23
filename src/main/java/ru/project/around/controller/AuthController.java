package ru.project.around.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.project.around.model.*;
import ru.project.around.service.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthTokens>> login(@RequestBody @Valid final UserCredentials userCredentials) {
        return authService.isCredentialsValid(userCredentials).flatMap(isValid -> {
            if (isValid) {
                return authService.createLoginTokens(userCredentials).map(ResponseEntity::ok);
            } else {
                return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
            }
        });
    }

    @PostMapping("/registration")
    public Mono<ResponseEntity<User>> registration(@RequestBody @Valid final RegistrationParams registrationParams) {
        return authService.isRegistrationDataValid(registrationParams).flatMap(isValid -> {
            if (isValid) {
                return authService.createNewUser(registrationParams).map(ResponseEntity::ok);
            } else {
                return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
            }
        });
    }

    @PostMapping("/tokens/refresh")
    public Mono<ResponseEntity<AuthTokens>> refreshTokens(@RequestBody @Valid final RefreshTokensParams refreshTokensParams) {
        final String refreshToken = refreshTokensParams.getRefreshToken();

        return authService.isTokenValid(refreshToken).flatMap(isValid -> {
            if (isValid) {
                return authService.refreshTokens(refreshTokensParams).map(ResponseEntity::ok);
            } else {
                return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
            }
        });
    }
}
