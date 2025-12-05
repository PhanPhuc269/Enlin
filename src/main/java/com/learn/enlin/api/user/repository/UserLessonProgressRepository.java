package com.learn.enlin.api.user.repository;

import com.learn.enlin.api.user.entity.UserLessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLessonProgressRepository extends JpaRepository<UserLessonProgress, UserLessonProgress.UserLessonId> {
}
