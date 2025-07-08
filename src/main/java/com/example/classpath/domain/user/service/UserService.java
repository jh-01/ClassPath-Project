package com.example.classpath.domain.user.service;

import com.example.classpath.domain.user.dto.request.UserCreateRequest;
import com.example.classpath.domain.user.dto.response.UserCreateResponse;
import com.example.classpath.domain.user.entity.User;
import com.example.classpath.domain.user.repository.UserRepository;
import com.example.classpath.global.exception.CustomException;
import com.example.classpath.global.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserCreateResponse saveUser(UserCreateRequest request) {
//        if(userRepository.existsByUserNumber(request.getUserNumber())){
//            throw new CustomException(ErrorType.DUPLICATED_USER_NUMBER);
//        }

        User user = User.builder()
                .name(request.getName())
                .userNumber(request.getUserNumber())
                .password(request.getPassword())
                .role(request.getRole())
                .build();

        userRepository.save(user);

        User savedUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new CustomException(ErrorType.USER_NOT_FOUND)
        );

        return savedUser.toDto(savedUser);
    }
}
