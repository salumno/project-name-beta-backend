package ru.project.around.service;

import ru.project.around.model.entity.User;
import ru.project.around.model.marker.EntryProcessMarker;
import ru.project.around.model.marker.PhoneCheckProcessMarker;
import ru.project.around.model.params.*;

public interface AuthService {
    AuthTokens refreshTokens(final RefreshTokensParams refreshTokensParams);

    boolean isTokenValid(final String token);

    AuthTokens createLoginTokens(final UserCredentials userCredentials);

    boolean isCredentialsValid(final UserCredentials userCredentials);

    EntryProcessMarker serveEntryProcess(final AppEntryPoint appEntryPoint);

    PhoneCheckProcessMarker validatePhoneCheckCode(final PhoneCheckCodeParams phoneCheckCodeParams);

    User registerUser(final RegistrationParams registrationParams);
}
