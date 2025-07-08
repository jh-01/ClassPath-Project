package com.example.classpath.domain.lecture.controller;

import com.example.classpath.domain.lecture.dto.LectureCreateRequest;
import com.example.classpath.domain.lecture.dto.LectureResponse;
import com.example.classpath.domain.lecture.service.LectureService;
import com.example.classpath.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lectures")
@RequiredArgsConstructor
public class LectureController {
    private final LectureService lectureService;

    @PostMapping
    public ResponseEntity<ApiResponse<LectureResponse>> createLecture(@Valid @RequestBody LectureCreateRequest requestDto) {
        LectureResponse response = lectureService.createLecture(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("강의를 개설하였습니다.", response));
    }

    @DeleteMapping("{lectureId}")
    public ResponseEntity<ApiResponse<Void>> deleteLecture(@PathVariable Long lectureId) {
        lectureService.deleteLecture(lectureId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success("강의를 삭제했습니다.",null));
    }

}
