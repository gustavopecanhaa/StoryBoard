package com.gustavo.storyboard.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class BlockedCardHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean blockedStatus;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    private String reason;
    private LocalDateTime createdAt = LocalDateTime.now();
}