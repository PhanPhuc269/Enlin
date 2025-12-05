package com.learn.enlin.api.user.dto.request;

import lombok.Data;

@Data
public class UserSettingsRequest {
    private String theme;
    private String nativeLanguage;
    private String targetLevel;
}
