package com.example.classpath;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ClassPathApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClassPathApplication.class, args);
    }

}
