package com.example.classpath.domain.lecture.service;

import com.example.classpath.domain.lecture.dto.LectureCreateRequest;
import com.example.classpath.domain.lecture.dto.LectureResponse;
import com.example.classpath.domain.lecture.dto.LectureSearchCondition;
import com.example.classpath.domain.lecture.dto.LectureUpdateRequest;
import com.example.classpath.domain.lecture.entity.Lecture;
import com.example.classpath.domain.lecture.exception.LectureCodeAlreadyExistException;
import com.example.classpath.domain.lecture.exception.LectureNotFoundException;
import com.example.classpath.domain.lecture.exception.LectureTimeInvalidException;
import com.example.classpath.domain.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureService {
    private final LectureRepository lectureRepository;

    /**
     * 강의 개설 기능
     */
    @Transactional
    public LectureResponse createLecture(LectureCreateRequest requestDto) {
        // 강의 코드 중복 확인
        if(lectureRepository.existsByCode(requestDto.getCode())) throw new LectureCodeAlreadyExistException();
        // 강의 시작 시간과 종료 시간 유효성 검사
        if(!isValidLectureTime(requestDto.getStartTime(),requestDto.getEndTime()))
            throw new LectureTimeInvalidException();

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
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(LectureNotFoundException::new);
        lectureRepository.delete(lecture);
    }

    @Transactional
    public LectureResponse updateLecture(Long lectureId, LectureUpdateRequest requestDto) {
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(LectureNotFoundException::new);
        lecture.updateName(requestDto.getName());
        lecture.updateMaxEnrollment(requestDto.getMaxEnrollment());
        return new LectureResponse(lecture);
    }

    public Page<LectureResponse> searchLecture(LectureSearchCondition condition, Pageable pageable) {
        return lectureRepository.searchLecture(condition, pageable);
    }

    private boolean isValidLectureTime(LocalTime startTime, LocalTime endTime) {
        return startTime.isBefore(endTime);
    }

    public List<LectureResponse> getLectures() {
        return lectureRepository.findAll().stream().map(LectureResponse::new).toList();
    }
}
