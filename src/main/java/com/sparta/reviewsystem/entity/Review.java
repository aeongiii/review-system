package com.sparta.reviewsystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@Entity
@Table(name = "review",
        // productId와 userId의 조합이 중복되지 않도록 unique 걸어주기. 필수는 아님
        uniqueConstraints = @UniqueConstraint(name="uniqueReview", columnNames = {"productId", "userId"}))
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // 하나의 상품에 여러개의 리뷰가 달릴 수 있음.
    // FK의 경우 사용할 컬럼 이름을 name에 넣는다.
    @JoinColumn (name = "product_id", nullable = false)
    private Product product; // 사실상 객체를 참조한다.

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Float userScore;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @Column(nullable = false, columnDefinition = "TIMESTAMP(3)")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column // null 가능
    private String imageUrl;

    @PrePersist
    public void setDefaultCreatedAt() {
        if (this.createdAt == null) {
            // 나노초 설정
            this.createdAt = OffsetDateTime.now(ZoneOffset.UTC).withNano(0);
        }
    }
}
