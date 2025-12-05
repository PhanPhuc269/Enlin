package com.learn.enlin.api.lesson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.enlin.api.lesson.entity.*;
import com.learn.enlin.api.lesson.repository.LessonRepository;
import com.learn.enlin.api.lesson.repository.SentenceRepository;
import com.learn.enlin.api.lesson.repository.TopicRepository;
import com.learn.enlin.api.user.entity.User;
import com.learn.enlin.api.user.repository.UserRepository;
import com.learn.enlin.infrastructure.ai.client.AIAgentClient;
import com.learn.enlin.infrastructure.ai.prompt.LessonPromptGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final AIAgentClient aiAgentClient;
    private final LessonPromptGenerator promptGenerator;
    private final ObjectMapper objectMapper;
    
    private final TopicRepository topicRepository;
    private final LessonRepository lessonRepository;
    private final SentenceRepository sentenceRepository;
    private final UserRepository userRepository;

    @Transactional
    public TopicPlanResponse createTopicPlan(String topicPrompt) {
        // 1. Call AI to generate plan
        String prompt = promptGenerator.generatePlan(topicPrompt);
        String jsonResponse = aiAgentClient.generate(prompt);
        TopicPlanResponse plan = parseJson(jsonResponse, TopicPlanResponse.class);
//
//        // 2. Save to DB
//        User user = getCurrentUser();
//
//        Topic topic = new Topic();
//        topic.setName(plan.getTopic());
//        topic.setDescription(plan.getDescription());
//        topic.setOriginalPrompt(topicPrompt);
//        topic.setUser(user);
//        topic.setStatus(TopicStatus.READY); // Assuming AI always succeeds for now
//        topic = topicRepository.save(topic);
//
//        List<Lesson> lessons = new ArrayList<>();
//        for (int i = 0; i < plan.getLessons().size(); i++) {
//            TopicPlanResponse.LessonPlanDto dto = plan.getLessons().get(i);
//            Lesson lesson = new Lesson();
//            lesson.setTopic(topic);
//            lesson.setTitle(dto.getTitle());
//            lesson.setDescription(dto.getDescription());
//            try {
//                lesson.setDifficulty(DifficultyLevel.valueOf(dto.getDifficulty().toUpperCase()));
//            } catch (IllegalArgumentException e) {
//                lesson.setDifficulty(DifficultyLevel.BEGINNER); // Fallback
//            }
//            lesson.setOrderIndex(i);
//            lessons.add(lesson);
//            List<Sentence> sentences = dto.getSentences().stream().map(senDto -> {
//                Sentence s = new Sentence();
//                s.setLesson(lesson);
//                s.setContentVn(senDto.getVietnamese()); // Assuming 'vietnamese' maps to 'content' in entity
//                s.setContext(senDto.getContext());
//                s.setSuggestedEn(senDto.getSuggestedEn());
//                return s;
//            }).collect(Collectors.toList());
//
//            sentenceRepository.saveAll(sentences);
//        }
//        lessonRepository.saveAll(lessons);

        return plan;
    }

    @Transactional
    public TopicResponse.TopicDto saveNewTopicPlan(String topicPrompt, TopicPlanResponse plan) {
        User user = getCurrentUser();

        Topic topic = new Topic();
        topic.setName(plan.getTopic());
        topic.setDescription(plan.getDescription());
        topic.setOriginalPrompt(topicPrompt);
        topic.setUser(user);
        topic.setStatus(TopicStatus.READY);

        topic = topicRepository.save(topic);

        for (int i = 0; i < plan.getLessons().size(); i++) {
            TopicPlanResponse.LessonPlanDto dto = plan.getLessons().get(i);

            Lesson lesson = new Lesson();
            lesson.setTopic(topic);
            lesson.setTitle(dto.getTitle());
            lesson.setDescription(dto.getDescription());
            lesson.setOrderIndex(i);

            try {
                lesson.setDifficulty(DifficultyLevel.valueOf(dto.getDifficulty().toUpperCase()));
            } catch (Exception e) {
                lesson.setDifficulty(DifficultyLevel.BEGINNER);
            }

            // Lưu Lesson trước để có ID
            lesson = lessonRepository.save(lesson);

            Lesson finalLesson = lesson;
            List<Sentence> sentences = dto.getSentences().stream().map(senDto -> {
                Sentence s = new Sentence();
                s.setLesson(finalLesson);
                s.setContentVn(senDto.getVietnamese());
                s.setContext(senDto.getContext());
                s.setSuggestedEn(senDto.getSuggestedEn());
                return s;
            }).collect(Collectors.toList());

            sentenceRepository.saveAll(sentences);
        }

        TopicResponse.TopicDto dto = new TopicResponse.TopicDto();
        dto.setId(topic.getId());
        dto.setName(topic.getName());
        dto.setDescription(topic.getDescription());

        return dto;
    }

    @Transactional
    public LessonResponse getLessonContent(UUID lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        List<Sentence> sentences = sentenceRepository.findByLessonId(lessonId);

        if (sentences.isEmpty()) {
            // Generate content if empty
            String prompt = promptGenerator.generateSentences(
                    lesson.getTopic().getName(),
                    lesson.getTitle(),
                    lesson.getDifficulty().name()
            );
            String jsonResponse = aiAgentClient.generate(prompt);
            LessonResponse aiResponse = parseJson(jsonResponse, LessonResponse.class);

            sentences = aiResponse.getSentences().stream().map(dto -> {
                Sentence s = new Sentence();
                s.setLesson(lesson);
                s.setContentVn(dto.getVietnamese()); // Assuming 'vietnamese' maps to 'content' in entity
                s.setContext(dto.getContext());
                s.setSuggestedEn(dto.getSuggestedEn());
                return s;
            }).collect(Collectors.toList());

            sentenceRepository.saveAll(sentences);
        }

        // Map to Response
        LessonResponse response = new LessonResponse();
        response.setId(lesson.getId());
        response.setTopic(lesson.getTopic().getName());
        response.setSentences(sentences.stream().map(s -> {
            LessonResponse.SentenceDto dto = new LessonResponse.SentenceDto();
            dto.setVietnamese(s.getContentVn());
            dto.setContext(s.getContext());
            dto.setSuggestedEn(s.getSuggestedEn());
            return dto;
        }).collect(Collectors.toList()));

        return response;
    }

    @Transactional
    public LessonResponse generateNewSentences(UUID lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        // Generate content if empty
        String prompt = promptGenerator.generateSentences(
                lesson.getTopic().getName(),
                lesson.getTitle(),
                lesson.getDifficulty().name()
        );
        String jsonResponse = aiAgentClient.generate(prompt);
        LessonResponse aiResponse = parseJson(jsonResponse, LessonResponse.class);

        List<Sentence> sentences = aiResponse.getSentences().stream().map(dto -> {
            Sentence s = new Sentence();
            s.setLesson(lesson);
            s.setContentVn(dto.getVietnamese()); // Assuming 'vietnamese' maps to 'content' in entity
            s.setContext(dto.getContext());
            s.setSuggestedEn(dto.getSuggestedEn());
            return s;
        }).collect(Collectors.toList());

        sentenceRepository.saveAll(sentences);

        // Map to Response
        LessonResponse response = new LessonResponse();
        response.setId(lesson.getId());
        response.setTopic(lesson.getTopic().getName());
        response.setSentences(sentences.stream().map(s -> {
            LessonResponse.SentenceDto dto = new LessonResponse.SentenceDto();
            dto.setVietnamese(s.getContentVn());
            dto.setContext(s.getContext());
            dto.setSuggestedEn(s.getSuggestedEn());
            return dto;
        }).collect(Collectors.toList()));

        return response;
    }
    
    public List<Topic> getUserTopics() {

        return topicRepository.findByUserId(getCurrentUser().getId());
    }
    
    public List<Lesson> getTopicLessons(UUID topicId) {
        return lessonRepository.findByTopicId(topicId);
    }

    @Transactional
    public void deleteTopic(UUID topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));
        
        // Ensure user owns the topic
        if (!topic.getUser().getId().equals(getCurrentUser().getId())) {
            throw new RuntimeException("Unauthorized");
        }
        
        topicRepository.delete(topic);
    }

    private User getCurrentUser() {
        String userId = com.learn.enlin.utils.SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("User not authenticated");
        }
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private <T> T parseJson(String json, Class<T> clazz) {
        try {
            if (json.startsWith("```json")) {
                json = json.substring(7);
            }
            if (json.startsWith("```")) {
                json = json.substring(3);
            }
            if (json.endsWith("```")) {
                json = json.substring(0, json.length() - 3);
            }
            return objectMapper.readValue(json.trim(), clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }
}
