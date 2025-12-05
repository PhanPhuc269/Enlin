package com.learn.enlin.api.lesson;

import com.learn.enlin.api.lesson.entity.Lesson;
import com.learn.enlin.api.lesson.entity.Topic;
import com.learn.enlin.base.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping("/plan")
    public ApiResponse<TopicPlanResponse> createPlan(@RequestBody LessonRequest request) {
        TopicPlanResponse response = lessonService.createTopicPlan(request.getTopic());
        return ApiResponse.<TopicPlanResponse>builder()
                .result(response)
                .build();
    }

    @PostMapping("/plan/save")
    public ApiResponse<TopicResponse.TopicDto> savePlan(@RequestBody TopicRequest request) {
        TopicResponse.TopicDto response = lessonService.saveNewTopicPlan(request.getOriginalPrompt(), request.getTopicPlan());
        return ApiResponse.<TopicResponse.TopicDto>builder()
                .result(response)
                .build();
    }

    @GetMapping("/{id}/content")
    public ApiResponse<LessonResponse> getLessonContent(@PathVariable UUID id) {
        LessonResponse response = lessonService.getLessonContent(id);
        return ApiResponse.<LessonResponse>builder()
                .result(response)
                .build();
    }

    @PostMapping("/{id}/new-sentences")
    public ApiResponse<LessonResponse> generateNewSentences(@PathVariable UUID id) {
        LessonResponse response = lessonService.generateNewSentences(id);
        return ApiResponse.<LessonResponse>builder()
                .result(response)
                .build();
    }

    @GetMapping("/topics")
    public ApiResponse<List<TopicResponse.TopicDto>> getUserTopics() {
        List<Topic> topics = lessonService.getUserTopics();
        List<TopicResponse.TopicDto> dtos = topics.stream().map(t -> {
            TopicResponse.TopicDto dto = new TopicResponse.TopicDto();
            dto.setId(t.getId());
            dto.setName(t.getName());
            dto.setDescription(t.getDescription());
            return dto;
        }).collect(Collectors.toList());

        return ApiResponse.<List<TopicResponse.TopicDto>>builder()
                .result(dtos)
                .build();
    }

    @GetMapping("/topics/{topicId}/lessons")
    public ApiResponse<List<LessonDto>> getTopicLessons(@PathVariable UUID topicId) {
        List<Lesson> lessons = lessonService.getTopicLessons(topicId);
        List<LessonDto> dtos = lessons.stream().map(l -> {
            LessonDto dto = new LessonDto();
            dto.setId(l.getId());
            dto.setTitle(l.getTitle());
            dto.setDescription(l.getDescription());
            dto.setDifficulty(l.getDifficulty() != null ? l.getDifficulty().name() : null);
            dto.setOrderIndex(l.getOrderIndex());
            dto.setTotalSentences(l.getTotalSentences());
            return dto;
        }).collect(Collectors.toList());

        return ApiResponse.<List<LessonDto>>builder()
                .result(dtos)
                .build();
    }

    @DeleteMapping("/topics/{topicId}")
    public ApiResponse<Void> deleteTopic(@PathVariable UUID topicId) {
        lessonService.deleteTopic(topicId);
        return ApiResponse.<Void>builder()
                .message("Topic deleted successfully")
                .build();
    }
}
