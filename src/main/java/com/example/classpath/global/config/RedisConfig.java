//package com.example.classpath.global.config;
//
//import lombok.RequiredArgsConstructor;
//import org.hibernate.annotations.Any;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@RequiredArgsConstructor
//@Configuration
//public class RedisConfig {
//
//
//    @Value("${spring.data.redis.port}")
//    public int port;
//
//    @Value("${spring.data.redis.host}")
//    public String host;
//
//    // TCP 통신
//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory() {
//        // LettuceConnectionFactory를 사용하여 Lettuce로 Redis와의 연결을 진행
//        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
//        // Redis의 주소와 포트를 파라미터로 넣음
//    }
//
//    // 일반 객체를 Redis에 저장하기 위한 RedisTemplate 설정
//    // RedisTemplate의 경우 Redis와 연결된 커넥션 위에서 조작 가능하도록 메소드를 제공
//    // -> Spring Boot에서 Redis에 데이터를 추가 조회 등 작업을 수행
//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//
//        /*
//        직렬화 및 역직렬화 설정을 위해 setKeySerializer, setValueSerializer 옵션을 사용
//        직렬화 : Java 내부의 데이터들을 외부에서 사용 가능하도록 바이트 형태로 데이터를 변환하는 기술
//        역직렬화 : 바이트 데이터를 원래 Object로 변환하는 기술
//
//        복잡한 데이터 구조의 클래스의 객체라도 직렬화 기본 조건만 지키면 큰 작업 없이 바로 직렬화, 역직렬화가 가능
//        -> 데이터 타입이 자동적으로 맞춰지기 때문에 타입에 대해 크게 신경쓰지 않아도 된다는 장점이 있음
//         */
//
//        // Key 및 Hash Key를 문자열로 저장
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//
//        // Value 및 Hash Value는 JSON 형식으로 직렬화
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//
//        template.afterPropertiesSet();
//        return template;
//    }
//
//    // 문자열 전용 RedisTemplate
//    @Bean
//    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
//        StringRedisTemplate template = new StringRedisTemplate();
//        template.setConnectionFactory(connectionFactory);
//
//        // Key, Value 모두 문자열 직렬화 사용
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new StringRedisSerializer());
//
//        return template;
//    }
//}
