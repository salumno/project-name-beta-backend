package ru.project.around.model.params;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AppEntryPoint {
    @NotEmpty
    private String phone;
}
