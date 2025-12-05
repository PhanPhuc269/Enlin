package com.learn.enlin.api.vocabulary.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "flashcard_reviews")
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class FlashcardReview {

    @Id
    private UUID vocabId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "vocab_id")
    private Vocabulary vocabulary;

    @Column(name = "box_level", nullable = false)
    private Integer boxLevel; // Leitner system box (1-5)

    @Column(name = "next_review_at")
    private LocalDateTime nextReviewAt;

    @Column(name = "last_reviewed_at")
    private LocalDateTime lastReviewedAt;
}