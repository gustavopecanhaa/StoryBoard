package com.gustavo.storyboard.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class CardMovementHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne
    @JoinColumn(name = "from_column_id")
    private BoardColumn fromColumn;

    @ManyToOne
    @JoinColumn(name = "to_column_id")
    private BoardColumn toColumn;

    private LocalDateTime movedAt = LocalDateTime.now();
}
