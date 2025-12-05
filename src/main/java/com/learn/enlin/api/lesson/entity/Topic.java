package com.learn.enlin.api.lesson.entity;

import com.learn.enlin.api.user.entity.User;
import com.learn.enlin.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "topics")
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class Topic extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "original_prompt", columnDefinition = "TEXT")
    private String originalPrompt;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private TopicStatus status; // Enum: GENERATING, READY, FAILED

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    private List<Lesson> lessons;
}