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

    // 실제 값은 float이지만 반환할때는 int로 반환되도록 한다.
    public int getScore() {
        return score.intValue();
    }
}
