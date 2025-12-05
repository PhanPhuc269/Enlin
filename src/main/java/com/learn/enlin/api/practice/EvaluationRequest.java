package com.learn.enlin.api.practice;

import lombok.Data;
import java.util.UUID;

@Data
public class EvaluationRequest {
    private UUID sentenceId;
    private String vietnameseSentence;
    private String englishTranslation;
}
