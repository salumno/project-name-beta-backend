package ru.project.around.service;

import ru.project.around.model.*;
import ru.project.around.model.login.AppEntryPoint;

public interface AuthService {
    AuthTokens refreshTokens(final RefreshTokensParams refreshTokensParams);

    boolean isTokenValid(final String token);

    AuthTokens createLoginTokens(final UserCredentials userCredentials);

    boolean isCredentialsValid(final UserCredentials userCredentials);

    EntryProcessMarker serveEntryProcess(final AppEntryPoint appEntryPoint);

    PhoneCheckProcessMarker validatePhoneCheckCode(final PhoneCheckCodeParams phoneCheckCodeParams);

    User registerUser(final RegistrationParams registrationParams);
}
