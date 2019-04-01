package ru.project.around.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.around.model.BusinessCard;
import ru.project.around.model.BusinessCardsRequestParams;
import ru.project.around.service.BusinessCardService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/cards")
public class BusinessCardController {

    private final BusinessCardService businessCardService;

    @Autowired
    public BusinessCardController(final BusinessCardService businessCardService) {
        this.businessCardService = businessCardService;
    }

    @PostMapping
    public ResponseEntity<List<BusinessCard>> getBusinessCardsByUserIds(
            @RequestBody @Valid final BusinessCardsRequestParams businessCardsRequestParams) {
        return ResponseEntity.ok(businessCardService.getBusinessCardsByUserIds(businessCardsRequestParams));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<BusinessCard> getBusinessCardByUserId(@PathVariable @NotNull final Long userId) {
        return ResponseEntity.ok(businessCardService.getBusinessCardByUserId(userId));
    }
}
