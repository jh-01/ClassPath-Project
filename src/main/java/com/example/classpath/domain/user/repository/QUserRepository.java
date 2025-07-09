package com.example.classpath.domain.user.repository;

import com.example.classpath.domain.user.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface QUserRepository {
    // 학번으로 찾기
    UserResponse findUserByUserNumber(String userNumber);

    // 아이디로 찾기
    UserResponse findUserById(Long id);

    // 전체 페이징 조회
    Page<UserResponse> findAllUsers(Pageable pageable);
}
