package com.learn.enlin.api.vocabulary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.enlin.api.user.entity.User;
import com.learn.enlin.api.user.repository.UserRepository;
import com.learn.enlin.api.vocabulary.entity.FlashcardReview;
import com.learn.enlin.api.vocabulary.entity.Vocabulary;
import com.learn.enlin.api.vocabulary.repository.VocabularyRepository;
import com.learn.enlin.infrastructure.ai.client.AIAgentClient;
import com.learn.enlin.infrastructure.ai.prompt.VocabularyPromptGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VocabularyService {

    private final AIAgentClient aiAgentClient;
    private final VocabularyPromptGenerator promptGenerator;
    private final ObjectMapper objectMapper;
    
    private final VocabularyRepository vocabularyRepository;
    private final UserRepository userRepository;

    public VocabularyDto lookup(VocabularyLookupRequest request) {
        return fetchFromAI(request);
    }

    @Transactional
    public VocabularyDto addVocabulary(VocabularyLookupRequest request) {
        VocabularyDto dto = fetchFromAI(request);
        
        User user = getCurrentUser();
        
        Vocabulary vocab = new Vocabulary();
        vocab.setUser(user);
        vocab.setWord(dto.getWord());
        vocab.setMeaning(dto.getDefinition());
        vocab.setPartOfSpeech(dto.getPartOfSpeech());
        vocab.setPronunciation(dto.getPronunciation());
        vocab.setExamples(dto.getExamples());
        vocab.setContextSentence(request.getContextSentence());
        vocab.setDomainTag(request.getDomain());
        
        vocabularyRepository.save(vocab);
        
        return dto;
    }
    
    public List<Vocabulary> getUserVocabulary() {
        return vocabularyRepository.findByUserId(getCurrentUser().getId());
    }
    
    public void deleteVocabulary(UUID id) {
        vocabularyRepository.deleteById(id);
    }

    @Transactional
    public Vocabulary updateVocabulary(UUID id, VocabularyUpdateRequest request) {
        Vocabulary vocab = vocabularyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vocabulary not found"));
        
        if (request.getNotes() != null) {
            vocab.setNotes(request.getNotes());
        }
        if (request.getMeaning() != null) {
            vocab.setMeaning(request.getMeaning());
        }
        if (request.getExamples() != null) {
            vocab.setExamples(Collections.singletonList(request.getExamples()));
        }
        
        return vocabularyRepository.save(vocab);
    }

    public List<Vocabulary> getFlashcardsDue() {
        return vocabularyRepository.findDueFlashcards(getCurrentUser().getId(), LocalDateTime.now());
    }

    @Transactional
    public void submitFlashcardReview(UUID vocabId, boolean isCorrect) {
        Vocabulary vocab = vocabularyRepository.findById(vocabId)
                .orElseThrow(() -> new RuntimeException("Vocabulary not found"));
        
        FlashcardReview review = vocab.getFlashcardReview();
        if (review == null) {
            review = FlashcardReview.builder()
                    .vocabulary(vocab)
                    .boxLevel(1)
                    .build();
            vocab.setFlashcardReview(review);
        }
        
        if (isCorrect) {
            review.setBoxLevel(Math.min(review.getBoxLevel() + 1, 5));
        } else {
            review.setBoxLevel(1); // Reset to box 1 on failure
        }
        
        review.setLastReviewedAt(LocalDateTime.now());
        review.setNextReviewAt(calculateNextReview(review.getBoxLevel()));
        
        vocabularyRepository.save(vocab);
    }

    private LocalDateTime calculateNextReview(int boxLevel) {
        return switch (boxLevel) {
            case 1 -> LocalDateTime.now().plusDays(1);
            case 2 -> LocalDateTime.now().plusDays(3);
            case 3 -> LocalDateTime.now().plusDays(7);
            case 4 -> LocalDateTime.now().plusDays(14);
            case 5 -> LocalDateTime.now().plusDays(30);
            default -> LocalDateTime.now().plusDays(1);
        };
    }

    private VocabularyDto fetchFromAI(VocabularyLookupRequest request) {
        String prompt = promptGenerator.generate(
                request.getText(),
                request.getContextSentence(),
                request.getDomain()
        );
        String jsonResponse = aiAgentClient.generate(prompt);

        try {
            if (jsonResponse.startsWith("```json")) {
                jsonResponse = jsonResponse.substring(7);
            }
            if (jsonResponse.startsWith("```")) {
                jsonResponse = jsonResponse.substring(3);
            }
            if (jsonResponse.endsWith("```")) {
                jsonResponse = jsonResponse.substring(0, jsonResponse.length() - 3);
            }

            return objectMapper.readValue(jsonResponse.trim(), VocabularyDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response for vocabulary lookup", e);
        }
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
