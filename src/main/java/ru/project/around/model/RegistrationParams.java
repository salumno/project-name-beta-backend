package ru.project.around.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RegistrationParams {
    @NotEmpty
    private String email;
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
