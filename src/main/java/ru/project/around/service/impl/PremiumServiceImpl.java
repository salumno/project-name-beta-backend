package ru.project.around.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.around.model.BusinessCard;
import ru.project.around.model.BusinessCardSnooper;
import ru.project.around.repository.BusinessCardRepository;
import ru.project.around.repository.BusinessCardSnooperRepository;
import ru.project.around.service.PremiumService;
import ru.project.around.util.PremiumUserUtil;
import ru.project.around.util.SecurityContextUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PremiumServiceImpl implements PremiumService {

    private final BusinessCardRepository businessCardRepository;
    private final BusinessCardSnooperRepository businessCardSnooperRepository;

    private final PremiumUserUtil premiumUserUtil;
    private final SecurityContextUtils securityContextUtils;

    @Autowired
    public PremiumServiceImpl(final BusinessCardRepository businessCardRepository,
                              final BusinessCardSnooperRepository businessCardSnooperRepository,
                              final PremiumUserUtil premiumUserUtil,
                              final SecurityContextUtils securityContextUtils) {
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

    private boolean isBusinessCardSnooperExpired(final BusinessCardSnooper businessCardSnooper) {
        return businessCardSnooper.getExpirationTime() < System.currentTimeMillis();
    }
}
