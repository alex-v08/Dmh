package com.dmh.cardservice.controller;


import com.dmh.cardservice.entity.dto.CardDto;
import com.dmh.cardservice.entity.dto.CardRequestDto;
import com.dmh.cardservice.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts/{accountId}/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardDto> createCard(
            @PathVariable Long accountId,
            @Valid @RequestBody CardRequestDto cardRequestDto) {
        CardDto createdCard = cardService.createCard(accountId, cardRequestDto);
        return new ResponseEntity<>(createdCard, HttpStatus.CREATED);
    }

/**
 *
 * @param accountId ID de la cuenta.
 * @param cardId    ID de la tarjeta.
 * @return Detalles de la tarjeta.
 */

@GetMapping("/{cardId}")
public ResponseEntity<CardDto> getCardById(
        @PathVariable Long accountId,
        @PathVariable Long cardId) {
    CardDto card = cardService.getCardById(accountId, cardId);
    return new ResponseEntity<>(card, HttpStatus.OK);
}

    /**
     *
     * @param accountId ID de la cuenta.
     * @param cardId    ID de la tarjeta.
     * @return Respuesta sin contenido.
     */
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(
            @PathVariable Long accountId,
            @PathVariable Long cardId) {
        cardService.deleteCard(accountId, cardId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
