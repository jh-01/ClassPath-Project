package com.example.classpath.global.config;

import com.example.classpath.global.jwt.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {    // 시큐리티 필터체인 구성
        http.csrf(csrf -> csrf.disable())    // csrf 보호 비활성화. JWT는 세셙이 없기 때문에 불필요
                .sessionManagement(session ->     // 세션 생성 정책 설정
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))    // 정책을 세션을 설정하지 않음으로 결정, 매 용청마다 JWT로 인증 진행
                .authorizeHttpRequests(auth -> auth    // HTTP 요청에 대한 인가 정책 설정
                        .requestMatchers("/auth/login", "/users/register").permitAll()   // 설정한 경로는 인증 없이 접근 가능
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()    // 나머지 경로에 대해서는 인증된 사용자만 접근 가능하도록 설정
                ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);    // 기본 체인 필터에서 Username 필터앞에 jwtAuth 필터 추가

        return http.build();    // 지금까지 http에 설정한 정책들을 기반으로 SecurityFilterChain을 빌드해서 반환해서 빈으로 등록
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();    // JWT 기반 인증 외에 필요 시 인증 매니저를 통해 인증 로직을 처리할 때 사용
    }
}
