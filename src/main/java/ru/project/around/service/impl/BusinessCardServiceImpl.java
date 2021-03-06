package ru.project.around.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.around.model.entity.BusinessCard;
import ru.project.around.model.entity.BusinessCardSnooper;
import ru.project.around.model.entity.FavoriteCard;
import ru.project.around.model.notification.PremiumAccountSnoopingNotification;
import ru.project.around.repository.BusinessCardRepository;
import ru.project.around.repository.BusinessCardSnooperRepository;
import ru.project.around.repository.FavoriteCardsRepository;
import ru.project.around.service.BusinessCardService;
import ru.project.around.util.PremiumUserUtil;
import ru.project.around.util.SecurityContextUtils;
import ru.project.around.util.factory.BusinessCardSnooperFactory;
import ru.project.around.websocket.PremiumAccountSnoopingHandler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BusinessCardServiceImpl implements BusinessCardService {

    private final BusinessCardRepository businessCardRepository;
    private final FavoriteCardsRepository favoriteCardsRepository;
    private final BusinessCardSnooperRepository businessCardSnooperRepository;

    private final PremiumAccountSnoopingHandler premiumAccountSnoopingHandler;

    private final BusinessCardSnooperFactory businessCardSnooperFactory;

    private final SecurityContextUtils securityContextUtils;
    private final PremiumUserUtil premiumUserUtil;

    @Autowired
    public BusinessCardServiceImpl(final BusinessCardRepository businessCardRepository,
                                   final FavoriteCardsRepository favoriteCardsRepository,
                                   final BusinessCardSnooperRepository businessCardSnooperRepository,
                                   final PremiumAccountSnoopingHandler premiumAccountSnoopingHandler,
                                   final BusinessCardSnooperFactory businessCardSnooperFactory,
                                   final SecurityContextUtils securityContextUtils,
                                   final PremiumUserUtil premiumUserUtil) {
        this.businessCardRepository = businessCardRepository;
        this.favoriteCardsRepository = favoriteCardsRepository;
        this.businessCardSnooperRepository = businessCardSnooperRepository;
        this.premiumAccountSnoopingHandler = premiumAccountSnoopingHandler;
        this.businessCardSnooperFactory = businessCardSnooperFactory;
        this.securityContextUtils = securityContextUtils;
        this.premiumUserUtil = premiumUserUtil;
    }

    @Override
    public BusinessCard getBusinessCardByUserId(final Long userId) {
        return businessCardRepository.findByUserId(userId);
    }

    @Override
    public BusinessCard getBusinessCardById(final Long cardId) {
        final BusinessCard businessCard = businessCardRepository.findById(cardId).orElseThrow(
                () -> new IllegalArgumentException("Business card with id " + cardId + " is not found.")
        );
        servePremiumUser(businessCard.getUserId(), securityContextUtils.getUserIdFromCurrentAuthToken());
        return businessCard;
    }

    private void servePremiumUser(final Long businessCardOwnerId, final Long currentUserId) {
        if (premiumUserUtil.isUserPremium(businessCardOwnerId)) {
            final BusinessCardSnooper businessCardSnooper = businessCardSnooperFactory.createSnooper(businessCardOwnerId, currentUserId);
            businessCardSnooperRepository.save(businessCardSnooper);
            premiumAccountSnoopingHandler.sendAccountSnoopingNotification(
                    PremiumAccountSnoopingNotification.builder()
                            .snoopedUserId(businessCardSnooper.getSnoopedUserId())
                            .snooperId(businessCardSnooper.getSnooperUserId())
                            .build()
            );
        }
    }

    @Override
    public BusinessCard updateBusinessCardInfo(final Long cardId, final BusinessCard updatedBusinessCard) {
        final BusinessCard businessCard = businessCardRepository.findById(cardId).orElseThrow(
                () -> new IllegalArgumentException("Business card with id " + cardId + " is not found.")
        );
        businessCard.setName(updatedBusinessCard.getName());
        businessCard.setSurname(updatedBusinessCard.getSurname());
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
        final Optional<BusinessCard> businessCardOptional = businessCardRepository.findById(cardId);
        final boolean isCardAlreadyAdded = favoriteCardsRepository.findByUserIdAndCardId(currentUserId, cardId) != null;
        if (businessCardOptional.isPresent() && !isCardAlreadyAdded) {
            final BusinessCard businessCard = businessCardOptional.get();
            if (!businessCard.getUserId().equals(currentUserId)) {
                final FavoriteCard favoriteCard = new FavoriteCard();
                favoriteCard.setUserId(currentUserId);
                favoriteCard.setCardId(cardId);
                favoriteCardsRepository.save(favoriteCard);
            }
        }
    }

    @Override
    public boolean isCardOperationAllowed(final Long cardId, final BusinessCard updatedBusinessCard) {
        final BusinessCard businessCard = businessCardRepository.findById(cardId).orElse(null);
        final Long currentUserId = securityContextUtils.getUserIdFromCurrentAuthToken();
        return businessCard != null && currentUserId.equals(businessCard.getUserId());
    }

    @Override
    public boolean isCardFavoriteForCurrentUser(final Long cardId) {
        final Long currentUserId = securityContextUtils.getUserIdFromCurrentAuthToken();
        final FavoriteCard favoriteCard = favoriteCardsRepository.findByUserIdAndCardId(currentUserId, cardId);
        return favoriteCard != null;
    }

    @Override
    public void removeCardFromFavorites(final Long cardId) {
        final Long currentUserId = securityContextUtils.getUserIdFromCurrentAuthToken();
        final FavoriteCard favoriteCard = favoriteCardsRepository.findByUserIdAndCardId(currentUserId, cardId);
        favoriteCardsRepository.delete(favoriteCard);
    }
}
