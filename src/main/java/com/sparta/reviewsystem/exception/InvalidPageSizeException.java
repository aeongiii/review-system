package com.sparta.reviewsystem.exception;

public class InvalidPageSizeException extends RuntimeException {

  // 페이지 사이즈가 잘못됐을 때
  public InvalidPageSizeException(String message) {
        super(message);
    }
}
