package com.sparta.reviewsystem.exception;

import org.springframework.http.HttpStatus;

public class InternalServerException extends CustomException{

    // 500대 - 서버 오류
    public InternalServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
