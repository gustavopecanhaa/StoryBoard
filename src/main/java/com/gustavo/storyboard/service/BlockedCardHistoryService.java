package com.gustavo.storyboard.service;

import com.gustavo.storyboard.model.BlockedCardHistory;
import com.gustavo.storyboard.model.Card;
import com.gustavo.storyboard.repository.BlockedCardHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockedCardHistoryService {
    private final BlockedCardHistoryRepository repository;

    public void logBlockAction(Card card, String reason, boolean blockedStatus) {
        BlockedCardHistory history = new BlockedCardHistory();
        history.setCard(card);
        history.setReason(reason);
        history.setBlockedStatus(blockedStatus);
        repository.save(history);
    }
}
