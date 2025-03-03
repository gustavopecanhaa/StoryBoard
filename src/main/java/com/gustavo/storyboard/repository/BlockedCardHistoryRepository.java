package com.gustavo.storyboard.repository;

import com.gustavo.storyboard.model.BlockedCardHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlockedCardHistoryRepository extends JpaRepository<BlockedCardHistory, Long> {

    @Query("SELECT h FROM BlockedCardHistory h WHERE h.card.column.storyBoard.id = :storyBoardId")
    List<BlockedCardHistory> findByStoryBoardId(@Param("storyBoardId") Long storyBoardId);
}