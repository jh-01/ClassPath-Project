package com.example.classpath.domain.user.service;

import com.example.classpath.domain.user.dto.UserRegisterRequestDto;
import com.example.classpath.domain.user.dto.UserRegisterResponse;
import com.example.classpath.domain.user.entity.Role;
import com.example.classpath.domain.user.entity.User;
import com.example.classpath.domain.user.repository.UserRepository;
import com.example.classpath.global.exception.CustomException;
import com.example.classpath.global.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegisterResponse registerUser(UserRegisterRequestDto request) {
        // 1. userNumber 중복 검사
        if (userRepository.existsByUserNumber(request.getUserNumber())) {
            throw new CustomException(ErrorType.DUPLICATE_USERNUMBER); // todo 커스텀 예외 처리를 만들어야한다.
        }
        // 2. 패스워드 인코딩
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        // 3. User 엔티티 생성
        User user = User.builder()
                .name(request.getName())
                .userNumber(request.getUserNumber())
                .password(encodedPassword)
                .role(Role.STUDENT)
                .build();
        // 4. 저장
        userRepository.save(user);

        // 저장한 유저 조회
        User savedUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new CustomException(ErrorType.USER_NOT_FOUND)
        );

        return savedUser.toDto(savedUser);
    }

}
