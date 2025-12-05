package com.learn.enlin.api.user.entity;

import com.learn.enlin.api.lesson.entity.Lesson;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_lesson_progress")
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class UserLessonProgress {

    @EmbeddedId
    private UserLessonId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("lessonId")
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "high_score")
    private Integer highScore;

    @Column(name = "last_practiced_at")
    private LocalDateTime lastPracticedAt;

    // Class ID nhúng
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLessonId implements Serializable {
        private UUID userId;
        private UUID lessonId;
    }
}