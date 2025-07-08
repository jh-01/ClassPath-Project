package com.example.classpath.domain.lecture.entity;

import com.example.classpath.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Lecture extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String code;

    @Column(name = "max_enrollment")
    private Integer maxEnrollment;

    @Column(name = "current_enrollment")
    private Integer currentEnrollment;

    @Column(name = "day_of_week")
    @Enumerated(value = EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;


    private Lecture(String name, String code, Integer maxEnrollment, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.code = code;
        this.maxEnrollment = maxEnrollment;
        this.currentEnrollment = 0;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static Lecture of(String name, String code, Integer maxEnrollment, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        return new Lecture(name, code, maxEnrollment, dayOfWeek, startTime, endTime);
    }

    public void updateName(String name) {
        if(StringUtils.hasText(name)) this.name = name;
    }

    public void updateMaxEnrollment(Integer maxEnrollment) {
        if(maxEnrollment != null && maxEnrollment > 0) this.maxEnrollment = maxEnrollment;
    }


}
