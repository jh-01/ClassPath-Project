package com.example.classpath.domain.user.dto;

import com.example.classpath.domain.user.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRegisterRequestDto {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "학번(교번)은 필수입니다.")
    private String userNumber;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,20}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~20자리여야 합니다."
    )

    private String password;

    @NotBlank(message = "역할은 필수입니다.")
    private Role role;
}
