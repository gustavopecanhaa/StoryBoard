package com.gustavo.storyboard.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime creationDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "column_id")
    private BoardColumn column;

    @Column(name = "column_entry_time")
    private LocalDateTime columnEntryTime = LocalDateTime.now();

    @OneToMany(mappedBy = "card")
    private List<BlockedCardHistory> blockedHistories = new ArrayList<>();

    @Column(name = "is_blocked", columnDefinition = "TINYINT(1)")
    private boolean isBlocked;
}