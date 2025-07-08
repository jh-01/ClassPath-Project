package com.example.classpath.global.jwt;


import com.example.classpath.domain.user.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

    @Value("${jwt.expiration}")
    private Long accessTokenExpiration;    // 3600000L = 1시간

    // 토큰 생성
    public String generateToken(Long userId, String userNumber, Role role) {
        Claims claims = Jwts.claims();    // CLaims는 JWT Payload 영역에 들어갈 데이터를 담는 Map 형식의 객체
        claims.put("userId", userId);
        claims.put("userNumber", userNumber);
        claims.put("role", role.name());    // 클레임에 사용자 정보를 키 밸류 형식으로 저장, 사용자 정보와 권한 데이터를 가지고 있음.
        Date now = new Date();    // 현재 시각 저장, 발급시각과 만료 시각 설정에 사용된다.
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);    // 만료 시각은 현재 시각 + 설정 시간으로 설정
        return Jwts.builder()    // jwt 빌더 패턴 시작
                .setClaims(claims)    // 페이로드 영역 설정
                .setIssuedAt(now)    // 발급시각 설정
                .setExpiration(expiryDate)    // 만료시각 설정
                .signWith(key, SignatureAlgorithm.HS256)    // 서명 설정, 키와 HS256 알고리즘으로 서명을 추가, 인증과 무결성 검사에 활용
                .compact();    // JWT를 문자열로 직렬화 후 생성 -> 반환 / compact는 빌더로 구성한 헤더 + 페이로드 + 서명을 Base64Url로 인코딩 한 후 . 으로 이어붙여 최종 JWT 문자열을 생성해준다. 이과정을 JWT 시리얼라이제이션 즉 직렬화라 한다.
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            // JWT에는 두가지 종류가 있는데  하나는 서명된 토큰 다른 하나는 서명되지 않은 토큰이다.
            // 서명되지 않은 토큰은 검증할 필요가 없는데 이 떄는 parseClaimsJWT를 사용하고 검증해야 하는 토큰은 parseClaimsJWS를 사용한다.
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);    // 토큰을 파싱하여 서명 검증, 만료시간 검증, 헤더 페이로드 포맷 검증을 통해 모두 일치하면 true를 아니라면 false를 반환
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 페이로드 영역에 있는 정보를 활용하기 위해 페이로드 영역을 꺼내주는 기능
    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();    // 위와 같은 과정을 거쳐서 토큰이 유효하다면 바디 영역 즉 페이로드 영역을 반환함.
        } catch (JwtException e) {
            throw new RuntimeException("JWT 관련 오류");    // todo 추후에 커스텀 예외로 처리 예정
        }
    }

}
