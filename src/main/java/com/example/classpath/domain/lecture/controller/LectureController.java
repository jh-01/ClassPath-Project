package com.example.classpath.domain.lecture.controller;

import com.example.classpath.domain.lecture.dto.LectureCreateRequest;
import com.example.classpath.domain.lecture.dto.LectureResponse;
import com.example.classpath.domain.lecture.dto.LectureSearchCondition;
import com.example.classpath.domain.lecture.dto.LectureUpdateRequest;
import com.example.classpath.domain.lecture.service.LectureService;
import com.example.classpath.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success("강의를 삭제했습니다.", null));
    }

    @PatchMapping("{lectureId}")
    public ResponseEntity<ApiResponse<LectureResponse>> updateLecture(@PathVariable Long lectureId,
                                                                      @Valid @RequestBody LectureUpdateRequest requestDto) {
        LectureResponse response = lectureService.updateLecture(lectureId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("강의 정보를 수정했습니다.", response));
    }


    /**
     * 강의명, 강의번호로 검색
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<LectureResponse>>> searchLecture(@ModelAttribute LectureSearchCondition condition,
                                                                            @PageableDefault(size = 20) Pageable pageable) {
        Page<LectureResponse> page = lectureService.searchLecture(condition, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("강의 정보를 조회했습니다.", page));
    }

    /**
     * 강의 단건 조회
     */
    @GetMapping("{lectureId}")
    public ResponseEntity<ApiResponse<LectureResponse>> getLecture(@PathVariable Long lectureId) {
        LectureResponse response = lectureService.getLecture(lectureId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("강의 정보를 조회했습니다.", response));
    }

    /**
     * 강의 전체 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<LectureResponse>>> getLectures() {
        List<LectureResponse> response = lectureService.getLectures();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("강의 정보를 조회했습니다.", response));
    }

    /**
     * 신청한 강의 조회
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<LectureResponse>>> getMyLectures(@RequestParam Long userId) { //TODO 추후에 @ContextHolder에서 꺼낼 예정
        List<LectureResponse> response = lectureService.getMyLectures(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("나의 강의 정보를 조회했습니다.", response));
    }



//    @GetMapping("/{lectureId}/users")
//    public ResponseEntity<ApiResponse<Page<LectureResponse>>>

}
