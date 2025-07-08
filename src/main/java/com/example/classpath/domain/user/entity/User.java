package com.example.classpath.domain.user.entity;

import com.example.classpath.domain.user.dto.response.UserCreateResponse;
import com.example.classpath.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "유저 이름은 필수입니다.")
    private String name;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "학번(교번)은 필수입니다.")
    private String userNumber;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-\\[\\]{};':\"\\\\|,.<>/?]).{8,20}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~20자리여야 합니다."
    )
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public UserCreateResponse toDto(User user){
        return new UserCreateResponse(
                user.getId(),
                user.getUserNumber(),
                user.getName(),
                user.getRole(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }
}