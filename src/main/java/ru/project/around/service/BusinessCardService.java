package ru.project.around.service;

import ru.project.around.model.BusinessCard;
import ru.project.around.model.BusinessCardsRequestParams;

import java.util.List;

public interface BusinessCardService {
    List<BusinessCard> getBusinessCardsByUserIds(final BusinessCardsRequestParams businessCardsRequestParams);

    BusinessCard getBusinessCardByUserId(final Long userId);
}
