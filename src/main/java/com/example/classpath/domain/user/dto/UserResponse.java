package com.example.classpath.domain.user.dto;

import com.example.classpath.domain.user.entity.Role;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String userNumber;
    private String name;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @QueryProjection
    public UserResponse(Long id, String userNumber, String name, Role role, LocalDateTime createdAt, LocalDateTime modifiedAt){
        this.id = id;
        this.userNumber = userNumber;
        this.name = name;
        this.role = role;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
