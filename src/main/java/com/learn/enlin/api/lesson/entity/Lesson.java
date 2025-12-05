package com.learn.enlin.api.lesson.entity;

import com.learn.enlin.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "lessons")
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class Lesson extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficulty; // Enum: BEGINNER, INTERMEDIATE, ADVANCED

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "total_sentences")
    private Integer totalSentences;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<Sentence> sentences;
}