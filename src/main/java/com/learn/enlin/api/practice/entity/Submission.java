package com.learn.enlin.api.practice.entity;

import com.learn.enlin.api.lesson.entity.Sentence;
import com.learn.enlin.api.user.entity.User;
import com.learn.enlin.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "submissions")
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class Submission extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sentence_id", nullable = false)
    private Sentence sentence;

    @Column(name = "user_translation", nullable = false, columnDefinition = "TEXT")
    private String userTranslation;

    private Integer score;

    // Lưu JSON phân tích lỗi từ AI
    // Cấu trúc: { mistakes: [{original: "...", correction: "..."}], explanation: "..." }
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ai_feedback_json", columnDefinition = "jsonb")
    private Map<String, Object> aiFeedbackJson;
}