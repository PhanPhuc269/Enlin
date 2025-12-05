package com.learn.enlin.api.vocabulary.entity;

import com.learn.enlin.api.lesson.entity.Sentence;
import com.learn.enlin.api.user.entity.User;
import com.learn.enlin.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "vocabularies")
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class Vocabulary extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String word;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String meaning;

    @Column(name = "part_of_speech", length = 50)
    private String partOfSpeech; // Noun, Verb, etc.

    private String pronunciation; // /həˈləʊ/

    // Lưu mảng các câu ví dụ
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> examples;

    // Lưu câu gốc mà người dùng đang dịch lúc highlight từ này
    @Column(name = "context_sentence", columnDefinition = "TEXT")
    private String contextSentence;

    // Link ngược lại bài học (Optional, dùng để điều hướng về bài cũ)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_sentence_id")
    private Sentence sourceSentence;

    @Column(name = "domain_tag")
    private String domainTag; // IT, Medical, etc.

    @Column(columnDefinition = "TEXT")
    private String notes;

    // Quan hệ 1-1 với tiến độ Flashcard
    @OneToOne(mappedBy = "vocabulary", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FlashcardReview flashcardReview;
}