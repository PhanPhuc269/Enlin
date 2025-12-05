package com.learn.enlin.api.lesson.repository;

import com.learn.enlin.api.lesson.entity.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SentenceRepository extends JpaRepository<Sentence, UUID> {
    List<Sentence> findByLessonId(UUID lessonId);
    long countByLessonId(UUID lessonId);
}
