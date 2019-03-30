package ru.project.around.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class BusinessCardsRequestParams {
    @NotEmpty
    private List<Long> userIds;
}
