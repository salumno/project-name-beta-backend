package ru.project.around.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserCredentials {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
