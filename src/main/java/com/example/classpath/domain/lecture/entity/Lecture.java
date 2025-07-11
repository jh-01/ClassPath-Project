package com.example.classpath.domain.lecture.entity;

import com.example.classpath.domain.enrollment.entity.Enrollment;
import com.example.classpath.domain.lecture.exception.LectureEnrollmentFullException;
import com.example.classpath.global.common.BaseEntity;
import com.example.classpath.global.exception.BusinessException;
import com.example.classpath.global.exception.ErrorType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

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
    // N+1문제가 발생하기 때문에 추후에 벌크 연산으로 바꾸는 방법이 있음
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.REMOVE)
    private Set<Enrollment> enrollments = new HashSet<>();


    public Lecture(String name, String code, Integer maxEnrollment, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
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

    public void enroll() {
        if (this.currentEnrollment >= this.maxEnrollment) {
            throw new BusinessException(ErrorType.LECTURE_ENROLLMENT_FULL);
        }
        this.currentEnrollment++;
    }

    public int getCurrentEnrollment() {
        if(this.currentEnrollment == null) return 0;
        else if(this.currentEnrollment > this.maxEnrollment)
            throw new BusinessException(ErrorType.LECTURE_ENROLLMENT_FULL);
        else return this.currentEnrollment;
    }

    public void cancel() {
        if(currentEnrollment == 0) throw new BusinessException(ErrorType.LECTURE_ENROLLMENT_EMPTY);
        if(currentEnrollment > 0) currentEnrollment--;
    }
}
