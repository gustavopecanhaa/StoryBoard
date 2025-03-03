package com.gustavo.storyboard.repository;

import com.gustavo.storyboard.model.StoryBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryBoardRepository extends JpaRepository<StoryBoard, Long> {}