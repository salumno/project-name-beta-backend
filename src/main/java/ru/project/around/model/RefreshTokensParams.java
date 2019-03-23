package ru.project.around.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RefreshTokensParams {
    @NotEmpty
    private String refreshToken;
}
