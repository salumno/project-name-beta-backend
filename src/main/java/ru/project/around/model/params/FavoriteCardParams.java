package ru.project.around.model.params;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FavoriteCardParams {
    @NotNull
    private Long cardId;
}
