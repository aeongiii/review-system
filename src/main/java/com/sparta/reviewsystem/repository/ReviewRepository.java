package com.sparta.reviewsystem.repository;

import com.sparta.reviewsystem.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // productId와 userId가 같은 것이 존재하는지 (중복확인)
    boolean existsByProductIdAndUserId(Long productId, Long userId);

    // 해당 상품에 대한 모든 리뷰 반환
    List<Review> findByProductId(Long productId);
}
