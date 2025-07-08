package com.example.classpath.domain.user.repository;


import com.example.classpath.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 학번이 존재하는지 확인
    boolean existsByUserNumber(String userNumber);

    Optional<User> findByUserNumber(String userNumber);
}
