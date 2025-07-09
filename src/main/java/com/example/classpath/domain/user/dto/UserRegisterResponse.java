package com.example.classpath.domain.user.dto;

import com.example.classpath.domain.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UserRegisterResponse {
    private Long id;
    private String userNumber;
    private String name;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
