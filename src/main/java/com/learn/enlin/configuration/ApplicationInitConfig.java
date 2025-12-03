package com.learn.enlin.configuration;

import com.learn.enlin.api.user.repository.UserRepository;
import com.learn.enlin.api.role.repository.RoleRepository; // 1. Import RoleRepository
import com.learn.enlin.api.user.entity.User;
import com.learn.enlin.enums.Role; // Đây là Enum
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    RoleRepository roleRepository;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {

                String roleName = Role.ADMIN.name();

                com.learn.enlin.api.role.entity.Role adminRole = roleRepository.findByName(roleName)
                        .orElseGet(() -> {
                            var newRole = com.learn.enlin.api.role.entity.Role.builder()
                                    .name(roleName)
                                    .description("Administrator role")
                                    .build();
                            return roleRepository.save(newRole);
                        });

                var roles = new HashSet<com.learn.enlin.api.role.entity.Role>();
                roles.add(adminRole);

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
            }
        };
    }
}