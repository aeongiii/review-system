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

        // 1. 상품이 존재하는지 먼저 확인
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다. productId : " + productId));

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

    // 리뷰 조회
    @Transactional
    public ReviewDto getReview(Long productId, Long cursor, int size) {

        // 1. 상품이 존재하는지 먼저 확인
        productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다. productId : " + productId));

        // 2. 페이징 설정
        Pageable pageable = PageRequest.of(0, size); // 0번부터 size까지의 페이지 정보 반환
        Slice<Review> reviewSlice;

        if (cursor == 0) { // 첫페이지 불러올 경우 최신순 정렬한 뒤 첫페이지 가져오기
            reviewSlice = reviewRepository.findByProduct_IdOrderByCreatedAtDesc(productId, pageable);
        } else { // 첫페이지 아닌 경우 cursor 값 기준으로 다음 페이지 가져오기
            reviewSlice = reviewRepository.findByProduct_IdAndCreatedAtBeforeOrderByCreatedAtDesc(productId, cursor, pageable);
        }

        // 반환된 reviewSlice를 ReviewResponseDto 리스트로 바꾸기
        // getContent() : 실제 데이터 리스트 반환
        List<ReviewResponseDto> reviewResponses = reviewSlice.getContent().stream()
                .map(review -> new ReviewResponseDto (
                        review.getId(),
                        review.getUserId(),
                        review.getUserScore(),
                        review.getContent(),
                        review.getImageUrl(),
                        review.getCreatedAt()
                )).toList();


        List<Review> allReview = reviewRepository.findByProductId(productId);

        // 총 리뷰 수 계산
        int totalReviewCount =allReview.size();

        // 총 리뷰 평균 계산
        float averageScore = (float) allReview.stream()
                .mapToDouble(Review::getUserScore)
                .average()
                .orElse(0.0);

        // 다음 값 있으면 nextCursor 값 반환
        Long nextCursor = reviewSlice.hasNext() ? reviewResponses.get(reviewResponses.size() -1).getId() : null;

        // 반환할 값 만들기
        ReviewDto reviewDto = new ReviewDto (totalReviewCount, (float) averageScore, nextCursor, reviewResponses);


        return reviewDto;
    }
}
