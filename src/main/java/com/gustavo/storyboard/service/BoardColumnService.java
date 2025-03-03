package com.gustavo.storyboard.service;

import com.gustavo.storyboard.model.BoardColumn;
import com.gustavo.storyboard.model.ColumnType;
import com.gustavo.storyboard.model.StoryBoard;
import com.gustavo.storyboard.repository.BoardColumnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardColumnService {
    private final BoardColumnRepository repository;

    public void createInitialColumns(StoryBoard board) {
        createColumn(board, "Introdução", ColumnType.INTRODUCTION, 1);
        createColumn(board, "Desenvolvimento", ColumnType.DEVELOPMENT, 2);
        createColumn(board, "Desfecho", ColumnType.ENDING, 1000);
    }

    private void createColumn(StoryBoard board, String name, ColumnType type, int order) {
        BoardColumn column = new BoardColumn();
        column.setStoryBoard(board);
        column.setName(name);
        column.setType(type);
        column.setColumnOrder(order);
        repository.save(column);
    }
}
