package ru.project.around.controller.premium;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.project.around.model.BusinessCard;
import ru.project.around.service.PremiumService;

import java.util.List;

@RestController
@RequestMapping("/premium")
public class PremiumUserController {

    private final PremiumService premiumService;

    public PremiumUserController(final PremiumService premiumService) {
        this.premiumService = premiumService;
    }

    @GetMapping("/snoopers")
    public ResponseEntity<List<BusinessCard>> getSnoopersBusinessCards() {
        if (premiumService.isPremiumOperationsAllowed()) {
            return ResponseEntity.ok(premiumService.getSnoopersBusinessCards());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
