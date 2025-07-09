package com.example.classpath.domain.user.service;

import com.example.classpath.domain.user.dto.UserChangePasswordRequest;
import com.example.classpath.domain.user.dto.UserRegisterRequestDto;
import com.example.classpath.domain.user.dto.UserRegisterResponse;
import com.example.classpath.domain.user.entity.Role;
import com.example.classpath.domain.user.entity.User;
import com.example.classpath.domain.user.repository.UserRepository;
import com.example.classpath.global.exception.BusinessException;
import com.example.classpath.global.exception.ErrorType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserRegisterResponse registerUser(UserRegisterRequestDto request) {
        // 1. userNumber 중복 검사
        if (userRepository.existsByUserNumber(request.getUserNumber())) {
            throw new BusinessException(ErrorType.DUPLICATE_USERNUMBER); // todo 커스텀 예외 처리를 만들어야한다.
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

        // 5. 저장한 유저 조회
        User savedUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new BusinessException(ErrorType.USER_NOT_FOUND)
        );

        return savedUser.toDto(savedUser);
    }

    // 유저 비밀번호 변경
    @Transactional
    public void changePassword(Long id, UserChangePasswordRequest request){
        // 1. 해당 유저 찾기
        User user = userRepository.findById(id).orElseThrow(
                () -> new BusinessException(ErrorType.USER_NOT_FOUND)
        );

        // 2. 비밀번호 검증
        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
            throw new BusinessException(ErrorType.INVALID_CREDENTIALS);
        }
        
        // 3. 비밀번호 암호화
        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());

        // 4. 비밀번호 업데이트
        user.changePassword(encodedNewPassword);

        userRepository.save(user);
    }

    // 유저 계정 삭제
    @Transactional
    public void deleteUser(Long id){
        // 해당 유저 있는지 확인
        User user = userRepository.findById(id).orElseThrow(
                () -> new BusinessException(ErrorType.USER_NOT_FOUND)
        );

        // 유저 삭제
        userRepository.delete(user);
    }
}
