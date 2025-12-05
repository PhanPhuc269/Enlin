package com.learn.enlin.api.vocabulary;

import lombok.Data;
import java.util.List;

@Data
public class VocabularyDto {
    private String word;
    private String partOfSpeech;
    private String definition;
    private String pronunciation;
    private List<String> examples;
}
