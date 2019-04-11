package ru.project.around.service;

import ru.project.around.model.BusinessCard;

import java.util.List;

public interface PremiumService {
    boolean isPremiumOperationsAllowed();

    List<BusinessCard> getSnoopersBusinessCards();
}
