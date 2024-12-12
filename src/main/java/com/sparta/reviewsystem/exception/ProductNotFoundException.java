package com.sparta.reviewsystem.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends CustomException{


    public ProductNotFoundException(String message){
        super(message, HttpStatus.BAD_REQUEST);
    }                             // 400
}
