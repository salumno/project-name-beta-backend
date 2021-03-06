package ru.project.around.model.params;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RegistrationParams {
    @NotEmpty
    private String registrationSessionId;

    @NotEmpty
    private String phone;

    @NotEmpty
    private String name;

    @NotEmpty
    private String surname;

    @NotEmpty
    private String password;

    @NotEmpty
    private String passwordCheck;
}
