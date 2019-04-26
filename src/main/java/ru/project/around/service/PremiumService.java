package ru.project.around.service;

import ru.project.around.model.entity.BusinessCard;

import java.util.List;

public interface PremiumService {
    boolean isPremiumOperationsAllowed();

    List<BusinessCard> getSnoopersBusinessCards();
}
