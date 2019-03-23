package ru.project.around.service;

import reactor.core.publisher.Mono;
import ru.project.around.model.*;

public interface AuthService {
    Mono<AuthTokens> refreshTokens(final RefreshTokensParams refreshTokensParams);

    Mono<Boolean> isTokenValid(final String token);

    Mono<AuthTokens> createLoginTokens(final UserCredentials userCredentials);

    Mono<Boolean> isCredentialsValid(final UserCredentials userCredentials);

    Mono<Boolean> isRegistrationDataValid(final RegistrationParams registrationParams);

    Mono<User> createNewUser(final RegistrationParams registrationParams);
}
