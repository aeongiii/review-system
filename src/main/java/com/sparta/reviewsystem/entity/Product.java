package com.sparta.reviewsystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reviewCount;

    @Column(nullable = false)
    private Float score;
}
