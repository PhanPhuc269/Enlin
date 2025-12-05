package com.learn.enlin.api.practice;

import com.learn.enlin.api.practice.entity.Submission;
import com.learn.enlin.base.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/practice")
@RequiredArgsConstructor
public class PracticeController {

    private final PracticeService practiceService;

    @PostMapping("/evaluate")
    public ApiResponse<EvaluationResponse> evaluate(@RequestBody EvaluationRequest request) {
        EvaluationResponse response = practiceService.evaluateTranslation(request);
        return ApiResponse.<EvaluationResponse>builder()
                .result(response)
                .build();
    }

    @GetMapping("/history")
    public ApiResponse<List<Submission>> getHistory(
            @RequestParam(required = false) Integer minScore,
            @RequestParam(required = false) Integer maxScore
    ) {
        return ApiResponse.<List<Submission>>builder()
                .result(practiceService.getHistory(minScore, maxScore))
                .build();
    }
}
