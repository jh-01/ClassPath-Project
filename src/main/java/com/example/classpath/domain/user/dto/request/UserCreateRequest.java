package com.example.classpath.domain.user.dto.request;

import com.example.classpath.domain.user.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCreateRequest {

    @NotBlank(message = "학번(교번)은 필수입니다.")
    private String userNumber;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-\\[\\]{};':\"\\\\|,.<>/?]).{8,20}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~20자리여야 합니다."
    )
    private String password;

    @NotBlank(message = "유저 이름은 필수입니다.")
    private String name;

    @NotNull(message = "역할(Role)은 필수입니다.")
    private Role role;
}

