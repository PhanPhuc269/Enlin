package com.learn.enlin.infrastructure.ai.prompt;

import org.springframework.stereotype.Component;

@Component
public class VocabularyPromptGenerator {

    public String generate(String text, String contextSentence, String domain) {
        return """
                You are a dictionary and language expert.
                Analyze the following word or phrase found in a specific context.
                
                Word/Phrase: "%s"
                Context Sentence: "%s"
                Domain/Topic: "%s"
                
                Task:
                1. Define the word/phrase in this specific context.
                2. Identify the part of speech.
                3. Provide the IPA pronunciation if possible.
                4. Generate 3 example sentences using this word/phrase in the same domain.
                
                Return the result strictly as a JSON object with the following structure:
                {
                  "word": "%s",
                  "partOfSpeech": "...",
                  "definition": "...",
                  "pronunciation": "/.../",
                  "examples": ["Example 1", "Example 2", "Example 3"]
                }
                Do not include any markdown formatting. Just the raw JSON string.
                """.formatted(text, contextSentence, domain, text);
    }
}
