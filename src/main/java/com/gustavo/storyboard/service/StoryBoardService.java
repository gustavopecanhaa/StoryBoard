package com.gustavo.storyboard.service;

import com.gustavo.storyboard.model.StoryBoard;
import com.gustavo.storyboard.repository.StoryBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoryBoardService {
    private final StoryBoardRepository repository;
    private final BoardColumnService columnService;

    public StoryBoard createStoryBoard(String name) {
        StoryBoard board = new StoryBoard();
        board.setName(name);
        repository.save(board);
        columnService.createInitialColumns(board);
        return board;
    }
}