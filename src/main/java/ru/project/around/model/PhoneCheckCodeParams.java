package ru.project.around.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PhoneCheckCodeParams {
    @NotEmpty
    private String registrationSessionId;
    @NotEmpty
    private String phone;
    @NotEmpty
    private String code;
}
