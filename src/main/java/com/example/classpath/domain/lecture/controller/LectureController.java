package com.example.classpath.domain.lecture.controller;

import com.example.classpath.domain.lecture.dto.*;
import com.example.classpath.domain.lecture.service.LectureService;
import com.example.classpath.domain.user.dto.UserRegisterResponse;
import com.example.classpath.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class LectureController {
    private final LectureService lectureService;

    @PostMapping("/admin/lectures")
    public ResponseEntity<ApiResponse<LectureResponse>> createLecture(@Valid @RequestBody LectureCreateRequest requestDto) {
        LectureResponse response = lectureService.createLecture(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("강의를 개설하였습니다.", response));
    }

    @DeleteMapping("/admin/lectures/{lectureId}")
    public ResponseEntity<ApiResponse<Void>> deleteLecture(@PathVariable Long lectureId) {
        lectureService.deleteLecture(lectureId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success("강의를 삭제했습니다.", null));
    }

    @PatchMapping("/admin/lectures/{lectureId}")
    public ResponseEntity<ApiResponse<LectureResponse>> updateLecture(@PathVariable Long lectureId,
                                                                      @Valid @RequestBody LectureUpdateRequest requestDto) {
        LectureResponse response = lectureService.updateLecture(lectureId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("강의 정보를 수정했습니다.", response));
    }


    /**
     * 강의명, 강의번호로 검색
     */
    @GetMapping("/lectures/search")
    public ResponseEntity<ApiResponse<Page<LectureResponse>>> searchLecture(@ModelAttribute LectureSearchCondition condition,
                                                                            @PageableDefault(size = 20) Pageable pageable) {
        Page<LectureResponse> page = lectureService.searchLecture(condition, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("강의 정보를 조회했습니다.", page));
    }

    /**
     * 강의 단건 조회
     */
    @GetMapping("/lectures/{lectureId}")
    public ResponseEntity<ApiResponse<LectureResponse>> getLecture(@PathVariable Long lectureId) {
        LectureResponse response = lectureService.getLecture(lectureId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("강의 정보를 조회했습니다.", response));
    }

    /**
     * 강의 전체 조회
     */
    @GetMapping("/lectures")
    public ResponseEntity<ApiResponse<List<LectureResponse>>> getLectures() {
        List<LectureResponse> response = lectureService.getLectures();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("강의 정보를 조회했습니다.", response));
    }

    /**
     * 신청한 강의 조회
     */
    @GetMapping("/lectures/me")
    public ResponseEntity<ApiResponse<List<LectureResponse>>> getMyLectures() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        List<LectureResponse> response = lectureService.getMyLectures(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("나의 강의 정보를 조회했습니다.", response));
    }

    /**
     * 강의별 수강생 목록 조회
     */
    @GetMapping("/admin/lectures/{lectureId}/students")
    public ResponseEntity<ApiResponse<Page<StudentResponse>>> getStudentsByLecture(@PathVariable Long lectureId,
                                                                                   @PageableDefault(size = 20) Pageable pageable) {
        Page<StudentResponse> page = lectureService.getStudentsByLecture(lectureId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("수강생 목록을 조회했습니다.", page));
    }

}
