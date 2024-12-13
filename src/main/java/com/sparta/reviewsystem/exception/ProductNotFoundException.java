package com.sparta.reviewsystem.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends CustomException{

    // 상품을 찾을 수 없을 때
    public ProductNotFoundException(String message){
        super(message, HttpStatus.BAD_REQUEST);
    }                             // 400
}
