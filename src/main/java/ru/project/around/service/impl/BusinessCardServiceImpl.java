package ru.project.around.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.around.model.BusinessCard;
import ru.project.around.model.BusinessCardsRequestParams;
import ru.project.around.repository.BusinessCardRepository;
import ru.project.around.service.BusinessCardService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessCardServiceImpl implements BusinessCardService {

    private final BusinessCardRepository businessCardRepository;

    @Autowired
    public BusinessCardServiceImpl(final BusinessCardRepository businessCardRepository) {
        this.businessCardRepository = businessCardRepository;
    }

    @Override
    public List<BusinessCard> getBusinessCardsByUserIds(final BusinessCardsRequestParams businessCardsRequestParams) {
        return businessCardsRequestParams.getUserIds().stream()
                .map(businessCardRepository::findByUserId)
                .collect(Collectors.toList());
    }

    @Override
    public BusinessCard getBusinessCardByUserId(final Long userId) {
        return businessCardRepository.findByUserId(userId);
    }
}
