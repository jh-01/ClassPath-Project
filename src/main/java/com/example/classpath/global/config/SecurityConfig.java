package com.example.classpath.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // csrf 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("users/register", "/login", "/css/**", "/js/**").permitAll() // 공개 경로
                        .anyRequest().authenticated() // 나머지는 인증 필요
                )
                .formLogin(Customizer.withDefaults()); // 기본 로그인 폼 사용

        return http.build();
    }
}
