package com.learn.enlin.api.lesson;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class TopicResponse {
    private List<TopicDto> topics;

    @Data
    public static class TopicDto {
        private UUID id;
        private String name;
        private String description;
    }
}
