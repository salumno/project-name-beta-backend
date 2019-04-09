package ru.project.around.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.around.model.BusinessCard;
import ru.project.around.model.FavoriteCardParams;
import ru.project.around.service.BusinessCardService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cards")
public class BusinessCardController {

    private final BusinessCardService businessCardService;

    @Autowired
    public BusinessCardController(final BusinessCardService businessCardService) {
        this.businessCardService = businessCardService;
    }

    @GetMapping("{id}")
    public ResponseEntity<BusinessCard> getBusinessCardById(@PathVariable("id") final Long cardId) {
        return ResponseEntity.ok(businessCardService.getBusinessCardById(cardId));
    }

    @PostMapping("{id}")
    public ResponseEntity<BusinessCard> updateBusinessCard(@PathVariable("id") final Long cardId,
                                                           @RequestBody final BusinessCard updatedBusinessCard) {
        if (businessCardService.isCardUpdateAllowed(cardId, updatedBusinessCard)) {
            return ResponseEntity.ok(businessCardService.updateBusinessCardInfo(cardId, updatedBusinessCard));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<BusinessCard> getBusinessCardByUserId(@PathVariable final Long userId) {
        return ResponseEntity.ok(businessCardService.getBusinessCardByUserId(userId));
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<BusinessCard>> getFavoritesCards() {
        return ResponseEntity.ok(businessCardService.getFavoriteBusinessCards());
    }

    @PostMapping("/favorites")
    public ResponseEntity addCardIntoFavorites(@RequestBody @Valid final FavoriteCardParams favoriteCardParams) {
        businessCardService.addCardIntoFavorites(favoriteCardParams.getCardId());
        return ResponseEntity.ok().build();
    }
}
