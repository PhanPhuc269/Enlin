package com.learn.enlin.api.vocabulary;

import com.learn.enlin.api.vocabulary.entity.Vocabulary;
import com.learn.enlin.base.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vocabulary")
@RequiredArgsConstructor
public class VocabularyController {

    private final VocabularyService vocabularyService;

    @PostMapping("/lookup")
    public ApiResponse<VocabularyDto> lookup(@RequestBody VocabularyLookupRequest request) {
        VocabularyDto response = vocabularyService.lookup(request);
        return ApiResponse.<VocabularyDto>builder()
                .result(response)
                .build();
    }

    @PostMapping
    public ApiResponse<VocabularyDto> add(@RequestBody VocabularyLookupRequest request) {
        VocabularyDto response = vocabularyService.addVocabulary(request);
        return ApiResponse.<VocabularyDto>builder()
                .result(response)
                .build();
    }

    @GetMapping
    public ApiResponse<List<Vocabulary>> list() {
        return ApiResponse.<List<Vocabulary>>builder()
                .result(vocabularyService.getUserVocabulary())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        vocabularyService.deleteVocabulary(id);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<Vocabulary> update(@PathVariable UUID id, @RequestBody VocabularyUpdateRequest request) {
        return ApiResponse.<Vocabulary>builder()
                .result(vocabularyService.updateVocabulary(id, request))
                .build();
    }

    @GetMapping("/flashcards")
    public ApiResponse<List<Vocabulary>> getFlashcards() {
        return ApiResponse.<List<Vocabulary>>builder()
                .result(vocabularyService.getFlashcardsDue())
                .build();
    }

    @PostMapping("/flashcards/{id}/review")
    public ApiResponse<Void> reviewFlashcard(@PathVariable UUID id, @RequestParam boolean isCorrect) {
        vocabularyService.submitFlashcardReview(id, isCorrect);
        return ApiResponse.<Void>builder()
                .message("Review submitted")
                .build();
    }
}
