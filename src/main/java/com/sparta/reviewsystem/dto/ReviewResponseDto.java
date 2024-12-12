package com.sparta.reviewsystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReviewResponseDto {
    private Long id;

    private Long userId;

    private Float score;

    private String content;

    private String imageUrl;

    private OffsetDateTime createdAt;


    public ReviewResponseDto(
            Long id, Long userId,
            Float score, String content,
            String imageUrl, OffsetDateTime  createdAt) {
        this.id = id;
        this.userId = userId;
        this.score = score;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

}
