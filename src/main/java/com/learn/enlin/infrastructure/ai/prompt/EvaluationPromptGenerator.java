package com.learn.enlin.infrastructure.ai.prompt;

import org.springframework.stereotype.Component;

@Component
public class EvaluationPromptGenerator {

    public String generate(String vietnamese, String englishTranslation) {
        return """
                You are an expert English teacher and translator.
                Evaluate the following translation from Vietnamese to English.
                
                Vietnamese Sentence: "%s"
                User's English Translation: "%s"
                
                Task:
                1. Score the translation on a scale of 0 to 100 based on accuracy, grammar, and naturalness.
                2. Identify any mistakes (Grammar, Vocabulary, Spelling, Style).
                3. Provide a corrected or better version of the translation.
                4. Explain the mistakes and why the correction is better.
                
                Return the result strictly as a JSON object with the following structure:
                {
                  "score": 85,
                  "mistakes": [
                    {
                      "type": "Grammar",
                      "segment": "wrong part",
                      "correction": "correct part",
                      "explanation": "Lý do sai"
                    }
                  ],
                  "correctedSentence": "The full corrected sentence.",
                  "explanations": ["Lý do chung 1", "Lý do chung 2"]
                }
                Do not include any markdown formatting. Just the raw JSON string.
                """.formatted(vietnamese, englishTranslation);
    }
}
