package ru.project.around.model;

import lombok.Data;

@Data
public class AuthTokens {
    private String accessToken;

    private String refreshToken;
}
