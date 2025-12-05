package com.learn.enlin.api.practice.repository;

import com.learn.enlin.api.practice.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
    List<Submission> findByUserId(UUID userId);
    List<Submission> findBySentenceIdAndUserId(UUID sentenceId, UUID userId);
    List<Submission> findByUserIdAndScoreBetween(UUID userId, int minScore, int maxScore);

    @Query("SELECT COUNT(DISTINCT s.sentence.id) FROM Submission s WHERE s.user.id = :userId AND s.sentence.lesson.id = :lessonId AND s.score >= :passingScore")
    long countPassedSentences(@Param("userId") UUID userId, @Param("lessonId") UUID lessonId, @Param("passingScore") int passingScore);
}
