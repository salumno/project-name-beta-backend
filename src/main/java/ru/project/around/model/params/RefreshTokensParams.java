package ru.project.around.model.params;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RefreshTokensParams {
    @NotEmpty
    private String refreshToken;
}
