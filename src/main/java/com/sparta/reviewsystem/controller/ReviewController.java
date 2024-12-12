package com.sparta.reviewsystem.controller;

import com.sparta.reviewsystem.dto.RequestDto;
import com.sparta.reviewsystem.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/products")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 리뷰 등록 - POST  /products/{productId}/reviews
    @PostMapping("/{productId}/reviews")
    public ResponseEntity<Void> createReview(
            @PathVariable Long productId,
            @RequestPart("requestDto") RequestDto requestDto,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        String imageUrl = (imageFile != null) ? dummy(imageFile.getOriginalFilename()) : null;
        reviewService.createReview(productId, requestDto, imageUrl);
        return ResponseEntity.ok().build();
    }


    // 가상 이미지 링크 반환
    private String dummy(String filename) {
        return "/image.png";
    }

}