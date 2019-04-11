package ru.project.around.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FavoriteCardParams {
    @NotNull
    private Long cardId;
}
