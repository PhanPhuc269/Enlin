package com.learn.enlin.api.lesson;

import lombok.Data;

import java.util.List;

@Data
public class TopicPlanResponse {
    private String topic;
    private String description;
    private List<LessonPlanDto> lessons;

    @Data
    public static class LessonPlanDto {
        private String title;
        private String description;
        private String difficulty;
        private List<LessonResponse.SentenceDto> sentences;
    }
}
