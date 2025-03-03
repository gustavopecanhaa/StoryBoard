package com.gustavo.storyboard.service;

import com.gustavo.storyboard.model.BoardColumn;
import com.gustavo.storyboard.model.CardMovementHistory;
import com.gustavo.storyboard.repository.CardMovementHistoryRepository;
import com.gustavo.storyboard.model.Card;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardMovementHistoryService {
    private final CardMovementHistoryRepository repository;

    public void logMovement(Card card, BoardColumn from, BoardColumn to) {
        CardMovementHistory history = new CardMovementHistory();
        history.setCard(card);
        history.setFromColumn(from);
        history.setToColumn(to);
        repository.save(history);
    }
}