package ru.project.around.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.around.model.entity.BusinessCard;
import ru.project.around.model.entity.BusinessCardSnooper;
import ru.project.around.model.entity.User;
import ru.project.around.model.status.UserPremiumStatus;
import ru.project.around.repository.BusinessCardRepository;
import ru.project.around.repository.BusinessCardSnooperRepository;
import ru.project.around.repository.UserRepository;
import ru.project.around.service.PremiumService;
import ru.project.around.util.PremiumUserUtil;
import ru.project.around.util.SecurityContextUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PremiumServiceImpl implements PremiumService {

    private final UserRepository userRepository;
    private final BusinessCardRepository businessCardRepository;
    private final BusinessCardSnooperRepository businessCardSnooperRepository;

    private final PremiumUserUtil premiumUserUtil;
    private final SecurityContextUtils securityContextUtils;

    @Autowired
    public PremiumServiceImpl(final UserRepository userRepository, final BusinessCardRepository businessCardRepository,
                              final BusinessCardSnooperRepository businessCardSnooperRepository,
                              final PremiumUserUtil premiumUserUtil,
                              final SecurityContextUtils securityContextUtils) {
        this.userRepository = userRepository;
        this.businessCardRepository = businessCardRepository;
        this.businessCardSnooperRepository = businessCardSnooperRepository;
        this.premiumUserUtil = premiumUserUtil;
        this.securityContextUtils = securityContextUtils;
    }

    @Override
    public boolean isPremiumOperationsAllowed() {
        return premiumUserUtil.isUserPremium(securityContextUtils.getUserIdFromCurrentAuthToken());
    }

    @Override
    public List<BusinessCard> getSnoopersBusinessCards() {
        final Long currentUserId = securityContextUtils.getUserIdFromCurrentAuthToken();
        final List<BusinessCardSnooper> snoopers = businessCardSnooperRepository.findAllBySnoopedUserId(currentUserId);
        return snoopers.stream()
                .filter(s -> !isBusinessCardSnooperExpired(s))
                .map(BusinessCardSnooper::getSnooperUserId)
                .distinct()
                .map(businessCardRepository::findByUserId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isPremiumUpdateAvailable(final Long userId) {
        final Long currentUserId = securityContextUtils.getUserIdFromCurrentAuthToken();
        return currentUserId.equals(userId);
    }

    @Override
    public boolean updatePremiumStatus(final Long userId) {
        final User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User with id " + userId + " is not found.")
        );
        final BusinessCard businessCard = businessCardRepository.findByUserId(userId);

        final UserPremiumStatus userPremiumStatus = user.getPremiumStatus();
        if (UserPremiumStatus.ACTIVATED.equals(userPremiumStatus)) {
            user.setPremiumStatus(UserPremiumStatus.AVAILABLE);
            businessCard.setPremium(false);
        } else if (UserPremiumStatus.AVAILABLE.equals(userPremiumStatus)) {
            user.setPremiumStatus(UserPremiumStatus.ACTIVATED);
            businessCard.setPremium(true);
        }

        userRepository.save(user);
        businessCardRepository.save(businessCard);

        return businessCard.getPremium();
    }

    private boolean isBusinessCardSnooperExpired(final BusinessCardSnooper businessCardSnooper) {
        return businessCardSnooper.getExpirationTime() < System.currentTimeMillis();
    }
}
