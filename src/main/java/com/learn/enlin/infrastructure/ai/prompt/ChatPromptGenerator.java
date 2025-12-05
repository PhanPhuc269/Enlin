package com.learn.enlin.infrastructure.ai.prompt;

import org.springframework.stereotype.Component;

@Component
public class ChatPromptGenerator {

    public String generate(String userMessage) {
        return """
                You are a helpful AI English tutor.
                Answer the user's question about English grammar, vocabulary, or writing.
                
                User Question: "%s"
                
                Keep the answer concise, helpful, and encouraging.
                """.formatted(userMessage);
    }
}
