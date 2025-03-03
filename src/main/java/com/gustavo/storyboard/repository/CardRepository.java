package com.gustavo.storyboard.repository;

import com.gustavo.storyboard.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("SELECT c FROM Card c WHERE c.column.storyBoard.id = :storyBoardId")
    List<Card> findByStoryBoardId(@Param("storyBoardId") Long storyBoardId);
}
