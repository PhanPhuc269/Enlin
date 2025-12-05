package com.learn.enlin.api.vocabulary.repository;

import com.learn.enlin.api.vocabulary.entity.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, UUID> {
    List<Vocabulary> findByUserId(UUID userId);

    @Query("SELECT v FROM Vocabulary v LEFT JOIN v.flashcardReview r WHERE v.user.id = :userId AND (r IS NULL OR r.nextReviewAt <= :now)")
    List<Vocabulary> findDueFlashcards(@Param("userId") UUID userId, @Param("now") LocalDateTime now);
}
