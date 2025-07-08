package com.example.classpath.domain.lecture.service;

import com.example.classpath.domain.lecture.dto.LectureCreateRequest;
import com.example.classpath.domain.lecture.dto.LectureResponse;
import com.example.classpath.domain.lecture.entity.Lecture;
import com.example.classpath.domain.lecture.repository.LectureRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureService {
    //TODO 각 예외는 추후 커스텀 예외로 변경 예정
    private final LectureRepository lectureRepository;

    /**
     * 강의 개설 기능
     */
    @Transactional
    public LectureResponse createLecture(LectureCreateRequest requestDto) {
        // 강의 코드 중복 확인
        if(lectureRepository.existsByCode(requestDto.getCode())) throw new IllegalArgumentException("강의 코드 중복");
        // 강의 시작 시간과 종료 시간 유효성 검사
        if(!isValidLectureTime(requestDto.getStartTime(),requestDto.getEndTime()))
            throw new IllegalArgumentException("강의 시작 시간은 종료 시간보다 이전이어야합니다.");

        Lecture lecture = Lecture.of(
                requestDto.getName(),
                requestDto.getCode(),
                requestDto.getMaxEnrollment(),
                requestDto.getDayOfWeek(),
                requestDto.getStartTime(),
                requestDto.getEndTime()
        );

        lectureRepository.save(lecture);

        return new LectureResponse(lecture);
    }

    @Transactional
    public void deleteLecture(Long lectureId) {
        //TODO 강의 삭제 시 Enrollment도 삭제 (Persist.REMOVE 사용해서)
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(() -> new EntityNotFoundException("강의가 존재하지 않습니다."));
        lectureRepository.delete(lecture);
    }

    private boolean isValidLectureTime(LocalTime startTime, LocalTime endTime) {
        return startTime.isBefore(endTime);
    }
}
