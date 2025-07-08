package com.example.classpath.domain.lecture.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter // @ModalAttribute
@NoArgsConstructor
public class LectureSearchCondition {
    private String name;
    private String code;
}
