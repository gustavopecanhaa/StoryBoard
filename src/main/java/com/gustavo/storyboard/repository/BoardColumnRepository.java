package com.gustavo.storyboard.repository;

import com.gustavo.storyboard.model.BoardColumn;
import com.gustavo.storyboard.model.StoryBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
    List<BoardColumn> findByStoryBoardOrderByColumnOrderAsc(StoryBoard storyBoard);
}