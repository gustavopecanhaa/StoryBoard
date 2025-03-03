package com.gustavo.storyboard.service;

import com.gustavo.storyboard.model.BoardColumn;
import com.gustavo.storyboard.model.Card;
import com.gustavo.storyboard.repository.BlockedCardHistoryRepository;
import com.gustavo.storyboard.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final CardMovementHistoryService movementHistoryService;

    public Card createCard(BoardColumn column, String title, String description) {
        Card card = new Card();
        card.setTitle(title);
        card.setDescription(description);
        card.setColumn(column);
        return cardRepository.save(card);
    }

    public void moveCardToColumn(Card card, BoardColumn newColumn) {
        BoardColumn oldColumn = card.getColumn();
        card.setColumn(newColumn);
        card.setColumnEntryTime(LocalDateTime.now());
        cardRepository.save(card);
        movementHistoryService.logMovement(card, oldColumn, newColumn);
    }
}
