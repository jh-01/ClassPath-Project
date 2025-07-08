package com.example.classpath.domain.user.controller;

import com.example.classpath.domain.user.dto.UserRegisterRequestDto;
import com.example.classpath.domain.user.dto.UserRegisterResponse;
import com.example.classpath.domain.user.service.UserService;
import com.example.classpath.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserRegisterResponse>> registerUser(
            @RequestBody @Valid UserRegisterRequestDto request
            ) {
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입이 완료되었습니다.", userService.registerUser(request)));
    }

}
