package com.sparta.reviewsystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

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

    // Formatter 선언
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

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

    // createAt을 문자열로 바꿔서 형식 올바른지 확인
    public String getCreatedAt() {
        // 포맷된 문자열 반환
        return createdAt.format(FORMATTER);

    }
}
