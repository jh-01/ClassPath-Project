package com.example.classpath.domain.lecture.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Lecture { //TODO BaseEntity 상속
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


    private Lecture(String name, String code, Integer maxEnrollment, Integer currentEnrollment, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.code = code;
        this.maxEnrollment = maxEnrollment;
        this.currentEnrollment = currentEnrollment;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static Lecture of(String name, String code, Integer maxEnrollment, Integer currentEnrollment, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        return new Lecture(name, code, maxEnrollment, currentEnrollment, dayOfWeek, startTime, endTime);
    }
}
