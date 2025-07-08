package com.example.classpath.domain.user.controller;

import com.example.classpath.domain.user.dto.request.UserCreateRequest;
import com.example.classpath.domain.user.dto.response.UserCreateResponse;
import com.example.classpath.domain.user.service.UserService;
import com.example.classpath.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 유저 생성
    @PostMapping("/register")
    public ApiResponse<UserCreateResponse> register(@RequestBody UserCreateRequest request) {
        return ApiResponse.success("회원가입이 완료되었습니다.", userService.saveUser(request));
    }

}
