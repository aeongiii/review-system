package com.sparta.reviewsystem.service;

import com.sparta.reviewsystem.dto.RequestDto;
import com.sparta.reviewsystem.entity.Product;
import com.sparta.reviewsystem.entity.Review;
import com.sparta.reviewsystem.repository.ProductRepository;
import com.sparta.reviewsystem.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ReviewService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    public ReviewService(ProductRepository productRepository, ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    // 리뷰 등록
    public void createReview(Long productId, RequestDto requestDto, String imageUrl) {

        // 1. 상품이 존재하는지 먼저 확인
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        // 2. 중복체크
        if (reviewRepository.existsByProductIdAndUserId(productId, requestDto.getUserId())) {
            throw new IllegalArgumentException("이미 리뷰를 작성했습니다.");
        }


        // 3. 리뷰 저장하기
        Review review = new Review();
        review.setProduct(product);
        review.setUserId(requestDto.getUserId());
        review.setContent(requestDto.getContent());
        review.setUserScore(requestDto.getScore());
        review.setImageUrl(imageUrl); // 입력받지 않았다면 null이 그대로 들어간다.

        // 작성시간 -> 알아서 찍혀 들어간다.
//        review.setCreateAt(requestDto.getCreateAt());

        reviewRepository.save(review);

        // 4. Product에 있는 내용도 수정한다.
        // 총 리뷰개수 +1
        product.setReviewCount(product.getReviewCount() + 1);

        // 총 평점 변경 : 해당 제품에 대한 리뷰 다 뽑아와서, 별점 새로 계산한다.
        List<Review> reviewList = reviewRepository.findByProductId(productId);
        float averageScore = (float) reviewList.stream()
                .mapToDouble(Review::getUserScore) // 각 객체에서 userScore 추출, .average() 사용하기 위해 double로 변환
                .average()
                .orElse(0.0);
        product.setScore(averageScore);

        // Product 저장(수정)
        productRepository.save(product);
        




    }




}
