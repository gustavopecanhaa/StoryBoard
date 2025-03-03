package com.gustavo.storyboard.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class StoryBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "storyBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardColumn> columns = new ArrayList<>();

    @Column(columnDefinition = "TINYINT(1)")
    private boolean isFinalized;
}