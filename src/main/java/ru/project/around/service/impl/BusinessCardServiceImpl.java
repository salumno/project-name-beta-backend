package ru.project.around.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.around.model.BusinessCard;
import ru.project.around.model.FavoriteCard;
import ru.project.around.repository.BusinessCardRepository;
import ru.project.around.repository.FavoriteCardsRepository;
import ru.project.around.service.BusinessCardService;
import ru.project.around.util.SecurityContextUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BusinessCardServiceImpl implements BusinessCardService {

    private final BusinessCardRepository businessCardRepository;
    private final FavoriteCardsRepository favoriteCardsRepository;
    private final SecurityContextUtils securityContextUtils;

    @Autowired
    public BusinessCardServiceImpl(final BusinessCardRepository businessCardRepository,
                                   final FavoriteCardsRepository favoriteCardsRepository,
                                   final SecurityContextUtils securityContextUtils) {
        this.businessCardRepository = businessCardRepository;
        this.favoriteCardsRepository = favoriteCardsRepository;
        this.securityContextUtils = securityContextUtils;
    }

    @Override
    public BusinessCard getBusinessCardByUserId(final Long userId) {
        return businessCardRepository.findByUserId(userId);
    }

    @Override
    public BusinessCard getBusinessCardById(final Long cardId) {
        return businessCardRepository.findById(cardId).orElseThrow(
                () -> new IllegalArgumentException("Business card with id " + cardId + " is not found.")
        );
    }

    @Override
    public BusinessCard updateBusinessCardInfo(final Long cardId, final BusinessCard updatedBusinessCard) {
        final BusinessCard businessCard = businessCardRepository.findById(cardId).orElseThrow(
                () -> new IllegalArgumentException("Business card with id " + cardId + " is not found.")
        );
        businessCard.setName(updatedBusinessCard.getName());
        businessCard.setSurname(updatedBusinessCard.getSurname());
        businessCard.setPhone(updatedBusinessCard.getPhone());
        return businessCardRepository.save(businessCard);
    }

    @Override
    public List<BusinessCard> getFavoriteBusinessCards() {
        final Long currentUserId = securityContextUtils.getUserIdFromCurrentAuthToken();
        return favoriteCardsRepository.findAllByUserId(currentUserId).stream()
                .map(favoriteCard -> businessCardRepository.findById(favoriteCard.getCardId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public void addCardIntoFavorites(final Long cardId) {
        final Long currentUserId = securityContextUtils.getUserIdFromCurrentAuthToken();
        final Optional<BusinessCard> businessCard = businessCardRepository.findById(cardId);
        if (businessCard.isPresent()) {
            final FavoriteCard favoriteCard = new FavoriteCard();
            favoriteCard.setUserId(currentUserId);
            favoriteCard.setCardId(cardId);
            favoriteCardsRepository.save(favoriteCard);
        }
    }

    @Override
    public boolean isCardUpdateAllowed(final Long cardId, final BusinessCard updatedBusinessCard) {
        final BusinessCard businessCard = businessCardRepository.findById(cardId).orElse(null);
        final Long currentUserId = securityContextUtils.getUserIdFromCurrentAuthToken();
        return businessCard != null && currentUserId.equals(businessCard.getUserId());
    }
}
