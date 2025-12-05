package com.learn.enlin.infrastructure.ai.prompt;

import org.springframework.stereotype.Component;

@Component
public class LessonPromptGenerator {

    public String generatePlan(String topicPrompt) {
        return """
                You are an expert English teacher.
                Create a study plan with 5-7 lessons for the topic: "%s".
                
                Requirements:
                - Lessons should progress from Beginner to Advanced difficulty.
                - Each lesson should have a title, a short description, and a target difficulty level.
                - Return the result strictly as a JSON object with the following structure:
                {
                  "topic": "Topic Name",
                  "description": "Brief description of the overall topic",
                  "original_prompt": "%s",
                  "lessons": [
                    {
                      "title": "Lesson Title",
                      "description": "Short description of what will be covered",
                      "sentences": [
                        {
                          "vietnamese": "...",
                          "context": "Optional context if needed",
                          "suggestedEn": "English translation"
                        },
                        ...
                      ]"
                    }
                  ]
                }
                Do not include any markdown formatting. Just the raw JSON string.
                """.formatted(topicPrompt, topicPrompt);
    }

    public String generateSentences(String topic, String lessonTitle, String difficulty) {
        return """
                You are an expert English teacher.
                Generate a list of 10 Vietnamese sentences for the lesson: "%s" (Topic: "%s", Difficulty: %s).
                These sentences will be used for a translation exercise (Vietnamese to English).
                
                Requirements:
                - The sentences should be natural and commonly used in the context of the topic.
                - Don't repeat sentences having in this lesson
                - Return the result strictly as a JSON object with the following structure:
                {
                  "sentences": [
                    {
                      "vietnamese": "...",
                      "context": "Optional context if needed",
                      "suggestedEn": "English translation"
                    }
                  ]
                }
                Do not include any markdown formatting. Just the raw JSON string.
                """.formatted(lessonTitle, topic, difficulty);
    }
}
