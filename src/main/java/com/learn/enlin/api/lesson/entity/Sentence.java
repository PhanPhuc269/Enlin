package com.learn.enlin.api.lesson.entity;

import com.learn.enlin.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sentences")
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class Sentence extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(name = "content_vn", nullable = false, columnDefinition = "TEXT")
    private String contentVn;

    @Column(columnDefinition = "TEXT")
    private String context;

    @Column(name = "suggested_en", columnDefinition = "TEXT")
    private String suggestedEn;

    @Column(name = "order_index")
    private Integer orderIndex;
}