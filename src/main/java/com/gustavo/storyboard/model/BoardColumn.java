package com.gustavo.storyboard.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class BoardColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "story_board_id")
    private StoryBoard storyBoard;

    private String name;

    @Enumerated(EnumType.STRING)
    private ColumnType type;

    private Integer columnOrder;

    @OneToMany(mappedBy = "column")
    private List<Card> cards = new ArrayList<>();
}
