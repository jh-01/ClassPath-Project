package com.example.classpath.domain.auth.service;

import com.example.classpath.domain.auth.dto.LoginRequestDto;
import com.example.classpath.domain.auth.dto.LoginResponseDto;
import com.example.classpath.domain.user.entity.User;
import com.example.classpath.domain.user.repository.UserRepository;
import com.example.classpath.global.jwt.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenProvider jwtTokenProvider;

    // 로그인 로직
    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByUserNumber(request.getUserNumber())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 학번입니다."));    // todo 예외처리

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");    // todo 예외처리
        }

        String accessToken = jwtTokenProvider.generateToken(
                user.getId(),
                user.getUserNumber(),
                user.getRole()
        );

        return new LoginResponseDto(accessToken);
    }
}
