package com.learn.enlin.api.lesson;

import lombok.Data;

import java.util.UUID;

@Data
public class LessonDto {
    private UUID id;
    private String title;
    private String description;
    private String difficulty;
    private Integer orderIndex;
    private Integer totalSentences;
}

