package com.example.classpath.domain.notice.controller;

import com.example.classpath.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 공지 작성

    // 공지 전체 조회

    // 공지 수정

    // 공지 삭제
}
