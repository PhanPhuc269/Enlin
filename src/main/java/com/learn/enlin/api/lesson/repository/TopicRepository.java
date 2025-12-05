package com.learn.enlin.api.lesson.repository;

import com.learn.enlin.api.lesson.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TopicRepository extends JpaRepository<Topic, UUID> {
    List<Topic> findByUserId(UUID userId);
}
