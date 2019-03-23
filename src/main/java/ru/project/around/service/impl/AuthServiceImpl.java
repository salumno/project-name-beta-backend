package ru.project.around.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.project.around.model.*;
import ru.project.around.repository.UserRepository;
import ru.project.around.service.AuthService;
import ru.project.around.util.jwt.JwtTokenUtils;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtTokenUtils tokenUtils;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public AuthServiceImpl(final JwtTokenUtils tokenUtils, final BCryptPasswordEncoder passwordEncoder, final UserRepository userRepository) {
        this.tokenUtils = tokenUtils;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public Mono<AuthTokens> refreshTokens(final RefreshTokensParams refreshTokensParams) {
        return userRepository.findById(
                tokenUtils.getUserId(refreshTokensParams.getRefreshToken())
        ).map(this::createFreshTokens);
    }

    @Override
    public Mono<Boolean> isTokenValid(final String token) {
        return Mono.just(tokenUtils.isTokenValid(token));
    }

    @Override
    public Mono<AuthTokens> createLoginTokens(final UserCredentials userCredentials) {
        return userRepository.findUserByEmail(userCredentials.getEmail())
                .map(this::createFreshTokens);
    }

    @Override
    public Mono<Boolean> isCredentialsValid(final UserCredentials userCredentials) {
        return userRepository.findUserByEmail(userCredentials.getEmail())
                .map(user -> passwordEncoder.matches(userCredentials.getPassword(), user.getHashPassword()))
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Boolean> isRegistrationDataValid(final RegistrationParams registrationParams) {
        return Mono.just(registrationParams.getPassword().equals(registrationParams.getPasswordCheck()));
    }

    @Override
    public Mono<User> createNewUser(final RegistrationParams registrationParams) {
        final User user = new User();
        // TODO: 23.03.19 Add user's name
        user.setEmail(registrationParams.getEmail());
        user.setPhone(registrationParams.getPhone());
        user.setHashPassword(passwordEncoder.encode(registrationParams.getPassword()));
        return userRepository.save(user);
    }

    private AuthTokens createFreshTokens(final User user) {
        final AuthTokens authTokens = new AuthTokens();
        authTokens.setAccessToken(tokenUtils.createAccessToken(user));
        authTokens.setRefreshToken(tokenUtils.createRefreshToken(user));
        return authTokens;
    }
}
