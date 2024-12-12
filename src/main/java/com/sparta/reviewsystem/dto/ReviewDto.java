package com.sparta.reviewsystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDto {

    private int totalCount;

    private float score;

    private Long cursor;

    private List<ReviewResponseDto> reviews;

    public ReviewDto(int totalCount, float score, Long cursor, List<ReviewResponseDto> reviews) {
        this.totalCount = totalCount;
        this.score = score;
        this.cursor = cursor;
        this.reviews = reviews;
    }


}
