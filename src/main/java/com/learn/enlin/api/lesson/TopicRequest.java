package com.learn.enlin.api.lesson;

import lombok.Data;

@Data
public class TopicRequest {
    private TopicPlanResponse topicPlan;
    private String originalPrompt;
}
