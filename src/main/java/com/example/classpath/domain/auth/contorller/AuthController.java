package com.example.classpath.domain.auth.contorller;

import com.example.classpath.domain.auth.dto.LoginRequestDto;
import com.example.classpath.domain.auth.dto.LoginResponseDto;
import com.example.classpath.domain.auth.service.AuthService;
import com.example.classpath.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @RequestParam LoginRequestDto request) {

        LoginResponseDto response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("로그인에 성공하였습니다.", response));
    }
}
