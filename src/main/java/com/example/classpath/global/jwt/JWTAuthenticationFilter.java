package com.example.classpath.global.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {    // 요청당 한 번만 실행되는 필터, 각 HTTP 요청 마다 jWT 인증 처리를 진행한다.

    private final JWTTokenProvider jwtTokenProvider;

    /**
     * OncePerRequestFilter 제공하는 추상 메서드
     * 요청 응답에서 JWT 로직을 처리하는 핵심 메서드
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");    // 헤더에서 Authorization을 꺼낸다. 베어러 토큰

        if (header != null && header.startsWith("Bearer ")) {    // Authorization이 비어있지않고 베어러로 시작한다면
            String token = header.substring(7);    // Bearer(공백) 총 7자 제외하고 추출해서 토큰에 담아서
            if (jwtTokenProvider.validateToken(token)) {    // 토큰 검증하고
                Claims claims = jwtTokenProvider.getClaims(token);    // 참이라면 토큰에서 페이로드 영역을 가져오고
                // 페이로드 영역 속 정보를 추출한다. 이 떄 반환 받을 타입을 지정해서 추출
                Long userId = claims.get("userId", Long.class);
                String userNumber = claims.get("userNumber", String.class);
                String role = claims.get("role", String.class);

                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userId,    // Principal
                                null,    // 비밀번호 JWT에서는 null 처리
                                authorities    // todo 권한 목록 ROLE 기반 인증 처리
                        );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));    // 인증 객체에 세션 고유 정보등을 추가로 기록
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);    // 스프링 컨텍스트에 인증 객체 등록 -> 스프링 컨텍스트를 통해 인증된 유저 정보 활용 가능
            }
        }
        filterChain.doFilter(request, response);    //    다음 필터로 넘기기, 토큰이 유효하지 않다면 등록하지 않고 넘어가고 유효하다면 정보를 가지고 있음.
    }
}
