package com.sparta.reviewsystem.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseDto {

    private Long id;

    private Long userId;

    private Float score;

    private String content;

    private String imageUrl;

    private LocalDateTime createdAt;

}
