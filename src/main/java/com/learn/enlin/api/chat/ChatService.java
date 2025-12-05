package com.learn.enlin.api.chat;

import com.learn.enlin.infrastructure.ai.client.AIAgentClient;
import com.learn.enlin.infrastructure.ai.prompt.ChatPromptGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final AIAgentClient aiAgentClient;
    private final ChatPromptGenerator promptGenerator;

    public ChatResponse chat(String userMessage) {
        String prompt = promptGenerator.generate(userMessage);
        String reply = aiAgentClient.generate(prompt);
        return new ChatResponse(reply);
    }
}
