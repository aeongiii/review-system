package com.sparta.reviewsystem.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CustomException {

    // 400대 - 클라이언트 오류
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}