package com.example.classpath.domain.notice.controller;

import com.example.classpath.domain.notice.dto.NoticeCreateRequestDto;
import com.example.classpath.domain.notice.dto.NoticeResponseDto;
import com.example.classpath.domain.notice.service.NoticeService;
import com.example.classpath.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 공지 생성
    @PostMapping
    public ResponseEntity<ApiResponse<NoticeResponseDto>> createNotice(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody NoticeCreateRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("공지사항이 생성되었습니다.", noticeService.createNotice(userDetails.getUsername(), requestDto.getTitle(), requestDto.getContents())));
    }
}
