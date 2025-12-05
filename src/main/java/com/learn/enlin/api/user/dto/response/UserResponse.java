package com.learn.enlin.api.user.dto.response;

import com.learn.enlin.api.role.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String username;
    String name;
    String email;
    String phone;
    LocalDate dob;
    String theme;
    String nativeLanguage;
    String targetLevel;
    Set<Role> roles;
}
