package com.example.classpath.domain.user.repository;

import com.example.classpath.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserNumber(String userNumber);
}
