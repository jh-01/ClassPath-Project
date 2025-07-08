package com.example.classpath.domain.user.repository;


import com.example.classpath.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
