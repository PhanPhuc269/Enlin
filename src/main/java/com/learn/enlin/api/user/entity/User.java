package com.learn.enlin.api.user.entity;

import com.learn.enlin.api.role.entity.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String username;
    String password;
    String name;

    @Column(unique = true)
    String email;

    @Column(unique = true)
    String phone;
    LocalDate dob;
    @ManyToMany(fetch = FetchType.EAGER)
    Set<Role> roles;
}
