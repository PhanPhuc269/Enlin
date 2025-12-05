package com.learn.enlin.api.practice;

import lombok.Data;
import java.util.List;

@Data
public class EvaluationResponse {
    private int score;
    private List<MistakeDto> mistakes;
    private String correctedSentence;
    private List<String> explanations;

    @Data
    public static class MistakeDto {
        private String type;
        private String segment;
        private String correction;
        private String explanation;
    }
}
