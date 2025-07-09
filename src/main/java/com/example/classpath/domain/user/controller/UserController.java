package com.example.classpath.domain.user.controller;

import com.example.classpath.domain.user.dto.*;
import com.example.classpath.domain.user.service.UserService;
import com.example.classpath.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable Long id
    ){
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(ApiResponse.success("유저 조회에 성공했습니다.", userService.findUserById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> getUserByNumber(
            @RequestParam String userNumber
    ){
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(ApiResponse.success("유저 조회에 성공했습니다.", userService.findUserByUserNumber(userNumber)));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @RequestBody UserFindRequest request
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("유저 목록 조회에 성공했습니다.", userService.findUsers(request)));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestBody UserChangePasswordRequest request
    ){
        // 로그인 기능 추가 후 현재 로그인한 계정 아이디값으로 변경
        userService.changePassword(1L, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("비밀번호 변경이 완료되었습니다.", null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteUser(){
        // 로그인 기능 추가 후 현재 로그인한 계정 아이디값으로 변경
        userService.deleteUser(1L);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("계정 삭제가 완료되었습니다.", null));
    }

}
