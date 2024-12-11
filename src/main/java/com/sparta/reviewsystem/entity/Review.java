package com.sparta.reviewsystem.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Review",
        // productId와 userId의 조합이 중복되지 않도록 unique 걸어주기. 필수는 아님
        uniqueConstraints = @UniqueConstraint(name="uniqueReview", columnNames = {"productId", "userId"}))
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // 하나의 상품에 여러개의 리뷰가 달릴 수 있음.
    // FK의 경우 사용할 컬럼 이름을 name에 넣는다.
    @JoinColumn(name = "productId", nullable = false)
    private Product product; // 사실상 객체를 참조한다.

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    // 그냥 객체 생성될때 들어가도 될 것 같다. 리뷰 수정할 일 없으니까
    private LocalDateTime createAt = LocalDateTime.now();

    @Column // null 가능
    private String imageUrl;
}