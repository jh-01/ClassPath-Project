package com.example.classpath.domain.notice.controller;

import com.example.classpath.domain.notice.dto.NoticeCreateRequestDto;
import com.example.classpath.domain.notice.dto.NoticeResponseDto;
import com.example.classpath.domain.notice.service.NoticeService;
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

@RestController
@RequestMapping
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 공지 생성
    @PostMapping("/admin/notices")
    public ResponseEntity<ApiResponse<NoticeResponseDto>> createNotice(@Valid @RequestBody NoticeCreateRequestDto requestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = (Long) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("공지사항이 생성되었습니다.", noticeService.createNotice(userId, requestDto.getTitle(), requestDto.getContents())));
    }

    // 공지 전체 조회
    @GetMapping("/notices")
    public ResponseEntity<ApiResponse<Page<NoticeResponseDto>>> getNotices(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("공지사항을 조회하였습니다.", noticeService.getNotices(pageable)));
    }

    // 공지 단일 조회
    @GetMapping("/notices/{id}")
    public ResponseEntity<ApiResponse<NoticeResponseDto>> getNotice(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("공지사항을 조회하였습니다.", noticeService.getNotice(id)));
    }

    // 공지 수정
    @PatchMapping("/admin/notices/{id}")
    public ResponseEntity<ApiResponse<NoticeResponseDto>> updateNotice(@PathVariable Long id, @RequestBody NoticeCreateRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("공지사항이 수정되었습니다.", noticeService.updateNotice(id, requestDto.getTitle(), requestDto.getContents())));
    }

    // 공지 삭제
    @DeleteMapping("/admin/notices/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotice(@PathVariable Long id){
        noticeService.deleteNotice(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("공지사항 삭제가 완료되었습니다.", null));
    }
}
