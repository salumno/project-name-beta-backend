package ru.project.around.service;

import ru.project.around.model.BusinessCard;

import java.util.List;

public interface BusinessCardService {
    BusinessCard getBusinessCardByUserId(final Long userId);

    BusinessCard getBusinessCardById(final Long cardId);

    BusinessCard updateBusinessCardInfo(final Long cardId, final BusinessCard updatedBusinessCard);

    List<BusinessCard> getFavoriteBusinessCards();

    void addCardIntoFavorites(final Long cardId);

    boolean isCardOperationAllowed(final Long cardId, final BusinessCard updatedBusinessCard);

    boolean isFavoriteCardsOperationAllowed(final Long cardId);

    void removeCardFromFavorites(final Long cardId);
}
