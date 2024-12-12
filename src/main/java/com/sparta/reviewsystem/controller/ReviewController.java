package com.sparta.reviewsystem.controller;

import com.sparta.reviewsystem.dto.RequestDto;
import com.sparta.reviewsystem.dto.ReviewDto;
import com.sparta.reviewsystem.service.ReviewService;
import org.springframework.http.MediaType;
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

    // 리뷰 등록 - POST                              contents-type이 "application/json" ,"multipart/form-data"가 아닌 요청은 처리하지 않는다.
    @PostMapping(value = "/{productId}/reviews", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> createReview(
            @PathVariable Long productId,
            @RequestPart("requestDto") RequestDto requestDto,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        String imageUrl = (imageFile != null) ? dummy(imageFile.getOriginalFilename()) : null;
        reviewService.createReview(productId, requestDto, imageUrl);

        return ResponseEntity.ok().build(); // 상태코드 200 반환
    }


    // 가상 이미지 링크 반환
    private String dummy(String filename) {
        return "/image.png";
    }


    // 리뷰 조회
    @GetMapping("/{productId}/reviews")
    public ResponseEntity<ReviewDto> getReview(
            @PathVariable Long productId,
            @RequestParam (value = "cursor") Long cursor,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        ReviewDto reviewDto = reviewService.getReview(productId, cursor, size);


        return ResponseEntity.ok(reviewDto); // 상태코드 200 + dto의 json 반환
    }

}
