package com.example.classpath.domain.user.service;

import com.example.classpath.domain.user.dto.UserRegistrationRequestDto;
import com.example.classpath.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.Encoder;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(UserRegistrationRequestDto request) {
        // 1. userNumber 중복 검사

        // 2. 패스워드 인코딩
        String encodedPassword = passwordEncoder.encode(request.getPassword())
        // 3. User 엔티티 생성

        // 4. 저장

    }

}
