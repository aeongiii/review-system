package com.sparta.reviewsystem.service;

import com.sparta.reviewsystem.dto.RequestDto;
import com.sparta.reviewsystem.dto.ReviewDto;
import com.sparta.reviewsystem.dto.ReviewResponseDto;
import com.sparta.reviewsystem.entity.Product;
import com.sparta.reviewsystem.entity.Review;
import com.sparta.reviewsystem.repository.ProductRepository;
import com.sparta.reviewsystem.repository.ReviewRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReviewService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    public ReviewService(ProductRepository productRepository, ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    // 리뷰 등록
    @Transactional
    public void createReview(Long productId, RequestDto requestDto, String imageUrl) {

        // 상품이 존재하는지 확인
        Product product = findProductById(productId);

        // 리뷰 중복체크
        if (reviewRepository.existsByProductIdAndUserId(productId, requestDto.getUserId())) {
            throw new IllegalArgumentException("이미 리뷰를 작성했습니다.");
        }

        // 리뷰 저장
        Review review = buildReview(product, requestDto, imageUrl);
        reviewRepository.save(review);

        // 총 리뷰개수 +1
        product.setReviewCount(product.getReviewCount() + 1);

        // 평균 점수 계산 및 저장
        calculateAndSaveAverageScore(productId, product);

    }

    // 리뷰 조회
    @Transactional
    public ReviewDto getReview(Long productId, Long cursor, int size) {

        // 상품이 존재하는지 확인
        Product product = findProductById(productId);

        // 평균 가져오기 & 리뷰 총 개수 계산
        float averageScore = product.getScore();
        Long totalReviewCount = reviewRepository.countByProductId(productId);

        // cursor값 기준으로 페이징해서 리스트로 가져오기
        List<ReviewResponseDto> reviewResponses = fetchPagedReviews(productId, cursor, size);

        // 다음에 반환할 리뷰 남았다면 nextCursor 반환. 리뷰 없다면 0 반환
        Long nextCursor = getNextCursor(reviewResponses);

        // 리턴
        return new ReviewDto (totalReviewCount, averageScore, nextCursor, reviewResponses);
    }


    // ===========


    // 리뷰 등록 전에 해당 상품이 있는지 확인
    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다. productId : " + productId));
    }

    // 리뷰 저장
    private Review buildReview(Product product, RequestDto requestDto, String imageUrl) {
        Review review = new Review();
        review.setProduct(product);
        review.setUserId(requestDto.getUserId());
        review.setContent(requestDto.getContent());
        review.setUserScore(requestDto.getScore());
        review.setImageUrl(imageUrl);
        return review;
    }

    // 평균값 계산하고 저장
    private void calculateAndSaveAverageScore(Long productId, Product product) {
        List<Review> allReviews = reviewRepository.findByProductId(productId);
        float averageScore = (float) allReviews.stream()
                .mapToDouble(Review::getUserScore)
                .average()
                .orElse(0.0);

        product.setScore(averageScore);
        productRepository.save(product);

    }

    // cursor값 기준으로 페이징해서 리스트로 가져오기
    private List<ReviewResponseDto> fetchPagedReviews(Long productId, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size);
        Slice<Review> reviewSlice;

        if (cursor == 0) {
            reviewSlice = reviewRepository.findByProduct_IdOrderByCreatedAtDesc(productId, pageable);
        } else {
            reviewSlice = reviewRepository.findByProduct_IdAndIdLessThanOrderByCreatedAtDesc(productId, cursor, pageable);
        }

        return reviewSlice.getContent().stream()
                .map(review -> new ReviewResponseDto(
                        review.getId(),
                        review.getUserId(),
                        review.getUserScore(),
                        review.getContent(),
                        review.getImageUrl(),
                        review.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    // 다음에 반환할 리뷰 남았다면 nextCursor 반환. 리뷰 없다면 0 반환
    private Long getNextCursor(List<ReviewResponseDto> reviews) {
        return reviews.isEmpty() ? 0 : reviews.get(reviews.size() - 1).getId();
    }



}
