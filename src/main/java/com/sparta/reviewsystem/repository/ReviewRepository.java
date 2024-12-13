package com.sparta.reviewsystem.repository;

import com.sparta.reviewsystem.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // productId와 userId가 같은 것이 존재하는지 (중복확인)
    boolean existsByProductIdAndUserId(Long productId, Long userId);

    // 해당 상품에 대한 모든 리뷰 반환
    List<Review> findByProductId(Long productId);

    // 인덱스 사용, cursor == 0일때, 해당 상품에 대한 리뷰 내림차순 정렬하고 첫번째 페이지 가져옴
    Slice<Review> findByProduct_IdOrderByCreatedAtDesc(Long productId,
                                                       Pageable pageable);

    // 인덱스 사용, cursor != 0일때. 기존 cursor값을 이용해서 다음 페이지 가져옴
    Slice<Review> findByProduct_IdAndIdLessThanOrderByCreatedAtDesc(
            Long productId, Long cursor, Pageable pageable);

    // 총 리뷰 수 계산
    Long countByProductId(Long productId);

}
