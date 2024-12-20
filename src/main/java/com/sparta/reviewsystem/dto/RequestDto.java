package com.sparta.reviewsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestDto {

    // 요청부에는 userId, score, content가 있다.
    private Long userId;

    private Float score;

    private String content;
}
