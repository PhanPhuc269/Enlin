package com.learn.enlin.api.practice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.enlin.api.lesson.entity.Sentence;
import com.learn.enlin.api.lesson.repository.SentenceRepository;
import com.learn.enlin.api.practice.entity.Submission;
import com.learn.enlin.api.practice.repository.SubmissionRepository;
import com.learn.enlin.api.user.entity.User;
import com.learn.enlin.api.user.entity.UserLessonProgress;
import com.learn.enlin.api.user.repository.UserLessonProgressRepository;
import com.learn.enlin.api.user.repository.UserRepository;
import com.learn.enlin.infrastructure.ai.client.AIAgentClient;
import com.learn.enlin.infrastructure.ai.prompt.EvaluationPromptGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PracticeService {

    private final AIAgentClient aiAgentClient;
    private final EvaluationPromptGenerator promptGenerator;
    private final ObjectMapper objectMapper;
    
    private final SubmissionRepository submissionRepository;
    private final SentenceRepository sentenceRepository;
    private final UserRepository userRepository;
    private final UserLessonProgressRepository userLessonProgressRepository;

    @Transactional
    public EvaluationResponse evaluateTranslation(EvaluationRequest request) {
        String prompt = promptGenerator.generate(
                request.getVietnameseSentence(),
                request.getEnglishTranslation()
        );
        String jsonResponse = aiAgentClient.generate(prompt);

        EvaluationResponse response;
        try {
            // Clean up potential markdown code blocks
            if (jsonResponse.startsWith("```json")) {
                jsonResponse = jsonResponse.substring(7);
            }
            if (jsonResponse.startsWith("```")) {
                jsonResponse = jsonResponse.substring(3);
            }
            if (jsonResponse.endsWith("```")) {
                jsonResponse = jsonResponse.substring(0, jsonResponse.length() - 3);
            }

            response = objectMapper.readValue(jsonResponse.trim(), EvaluationResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response for translation evaluation", e);
        }
        
        // Save Submission
        if (request.getSentenceId() != null) {
            Sentence sentence = sentenceRepository.findById(request.getSentenceId())
                    .orElseThrow(() -> new RuntimeException("Sentence not found"));
            
            User user = getCurrentUser();
            
            Submission submission = new Submission();
            submission.setUser(user);
            submission.setSentence(sentence);
            submission.setUserTranslation(request.getEnglishTranslation());
            submission.setScore(response.getScore());
            
            // Convert response to Map for JSON storage
            Map<String, Object> feedbackMap = objectMapper.convertValue(response, Map.class);
            submission.setAiFeedbackJson(feedbackMap);
            
            submissionRepository.save(submission);
            
            // Update Progress
            updateLessonProgress(user, sentence.getLesson(), response.getScore());
        }
        
        return response;
    }

    private void updateLessonProgress(User user, com.learn.enlin.api.lesson.entity.Lesson lesson, int score) {
        UserLessonProgress.UserLessonId id = new UserLessonProgress.UserLessonId(user.getId(), lesson.getId());
        UserLessonProgress progress = userLessonProgressRepository.findById(id)
                .orElse(UserLessonProgress.builder()
                        .id(id)
                        .user(user)
                        .lesson(lesson)
                        .isCompleted(false)
                        .highScore(0)
                        .build());

        progress.setLastPracticedAt(java.time.LocalDateTime.now());
        if (score > progress.getHighScore()) {
            progress.setHighScore(score);
        }

        // Check completion
        long totalSentences = sentenceRepository.countByLessonId(lesson.getId());
        long passedSentences = submissionRepository.countPassedSentences(user.getId(), lesson.getId(), 80); // 80 is passing score

        if (totalSentences > 0 && passedSentences >= totalSentences) {
            progress.setIsCompleted(true);
        }

        userLessonProgressRepository.save(progress);
    }

    public java.util.List<Submission> getHistory(Integer minScore, Integer maxScore) {
        User user = getCurrentUser();
        if (minScore != null && maxScore != null) {
            return submissionRepository.findByUserIdAndScoreBetween(user.getId(), minScore, maxScore);
        }
        return submissionRepository.findByUserId(user.getId());
    }

    private User getCurrentUser() {
        String userId = com.learn.enlin.utils.SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("User not authenticated");
        }
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
