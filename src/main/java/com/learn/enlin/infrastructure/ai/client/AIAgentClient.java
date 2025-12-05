package com.learn.enlin.infrastructure.ai.client;

public interface AIAgentClient {
    String generate(String prompt);
    String generate(String prompt, String systemInstruction);
}
