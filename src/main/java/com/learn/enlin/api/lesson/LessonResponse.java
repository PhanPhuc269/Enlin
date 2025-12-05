package com.learn.enlin.api.lesson;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class LessonResponse {
    private UUID id;
    private String topic;
    private List<SentenceDto> sentences;

    @Data
    public static class SentenceDto {
        private String vietnamese;
        private String context;
        private String suggestedEn;
    }
}
