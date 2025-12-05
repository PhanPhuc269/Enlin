package com.learn.enlin.infrastructure.ai.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Component
public class HttpAIAgentClient implements AIAgentClient {

    private final RestTemplate restTemplate;
    
    @Value("${ai.agent.url:http://localhost:3000/api/generate}") // Default URL, configure in yaml
    private String agentUrl;

    public HttpAIAgentClient() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String generate(String prompt) {
        return generate(prompt, null);
    }

    @Override
    public String generate(String prompt, String systemInstruction) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("prompt", prompt);
        if (systemInstruction != null) {
            requestBody.put("systemInstruction", systemInstruction);
        }

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            // Assuming the AI Agent returns a JSON with a "content" field or similar.
            // Adjust this based on your actual AI Agent's API contract.
            // For now, I'll assume it returns a raw string or a JSON object where we extract the text.
            // If it returns a JSON object like { "response": "..." }, we should map it.
            // Let's assume it returns a simple Map for flexibility.
            
            ResponseEntity<Map> response = restTemplate.postForEntity(agentUrl, request, Map.class);
            
            if (response.getBody() != null && response.getBody().containsKey("content")) {
                return (String) response.getBody().get("content");
            } else if (response.getBody() != null && response.getBody().containsKey("text")) {
                return (String) response.getBody().get("text");
            }
            
            // Fallback: return the whole body as string if structure is unknown
            return response.getBody() != null ? response.getBody().toString() : "";
            
        } catch (Exception e) {
            // Log error
            throw new RuntimeException("Failed to call AI Agent", e);
        }
    }
}
