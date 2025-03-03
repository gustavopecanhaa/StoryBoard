package com.gustavo.storyboard.repository;

import com.gustavo.storyboard.model.CardMovementHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gustavo.storyboard.model.Card;

import java.util.List;

public interface CardMovementHistoryRepository extends JpaRepository<CardMovementHistory, Long> {
    List<CardMovementHistory> findByCardOrderByMovedAtAsc(Card card);
}
