package com.learn.enlin.api.vocabulary;

import lombok.Data;

@Data
public class VocabularyLookupRequest {
    private String text;
    private String contextSentence;
    private String domain;
}
