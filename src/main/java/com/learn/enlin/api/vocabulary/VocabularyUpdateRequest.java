package com.learn.enlin.api.vocabulary;

import lombok.Data;

@Data
public class VocabularyUpdateRequest {
    private String notes;
    private String meaning;
    private String examples;
}
