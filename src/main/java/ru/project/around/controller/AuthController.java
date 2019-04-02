package ru.project.around.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.project.around.model.*;
import ru.project.around.model.login.AppEntryPoint;
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

    @PostMapping("/entry")
    public ResponseEntity<EntryProcessMarker> applicationEntryPoint(@RequestBody @Valid final AppEntryPoint appEntryPoint) {
        return ResponseEntity.ok(authService.serveEntryProcess(appEntryPoint));
    }

    @PostMapping("/phone/check")
    public ResponseEntity<PhoneCheckProcessMarker> validatePhoneCheckCode(@RequestBody @Valid final PhoneCheckCodeParams phoneCheckCodeParams) {
        return ResponseEntity.ok(authService.validatePhoneCheckCode(phoneCheckCodeParams));
    }

    @PostMapping("/registration")
    public ResponseEntity<User> registrationSubmit(@RequestBody @Valid final RegistrationParams registrationParams) {
        final User user = authService.registerUser(registrationParams);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokens> login(@RequestBody @Valid final UserCredentials userCredentials) {
        if (authService.isCredentialsValid(userCredentials)) {
            return ResponseEntity.ok(authService.createLoginTokens(userCredentials));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/tokens/refresh")
    public ResponseEntity<AuthTokens> refreshTokens(@RequestBody @Valid final RefreshTokensParams refreshTokensParams) {
        final String refreshToken = refreshTokensParams.getRefreshToken();
        if (authService.isTokenValid(refreshToken)) {
            return ResponseEntity.ok(authService.refreshTokens(refreshTokensParams));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
