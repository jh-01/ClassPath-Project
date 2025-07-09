package com.example.classpath.domain.user.controller;

import com.example.classpath.domain.user.dto.UserChangePasswordRequest;
import com.example.classpath.domain.user.dto.UserRegisterRequestDto;
import com.example.classpath.domain.user.dto.UserRegisterResponse;
import com.example.classpath.domain.user.service.UserService;
import com.example.classpath.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserRegisterResponse>> registerUser(
            @RequestBody @Valid UserRegisterRequestDto request
            ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입이 완료되었습니다.", userService.registerUser(request)));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<Void>> changePassword(
            HttpServletRequest servletRequest,
            @RequestBody UserChangePasswordRequest request
    ){
        // 로그인된 유저 아이디 가져오기
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        userService.changePassword(userId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("비밀번호 변경이 완료되었습니다.", null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteUser(HttpServletRequest servletRequest){
        // 로그인된 유저 아이디 가져오기
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        userService.deleteUser(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("계정 삭제가 완료되었습니다.", null));
    }
}