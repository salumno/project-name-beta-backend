package ru.project.around.model.params;

import lombok.Data;

@Data
public class AuthTokens {
    private Long userId;

    private String accessToken;

    private String refreshToken;
}
