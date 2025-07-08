package com.example.classpath.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
