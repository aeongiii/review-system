package com.sparta.reviewsystem.exception;

import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 에러 처리
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }

    // 400 에러 처리 - 상품을 찾을 수 없을 때
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }

    // 409 에러 처리 - 낙관적 락 버전 충돌 시
    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<String> handleOptimisticLockException(OptimisticLockException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("해당 상품의 정보가 변경되어 요청을 처리하지 못했습니다. 잠시 후 다시 시도해주세요.");
    }


    // 500 에러 처리
    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<String> handleInternalServerException(InternalServerException ex) {
        // 서버 내부에서만 사용
        System.err.println("InternalServerException 발생: " + ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).build(); // 상태 코드만 반환
    }

    // 페이지 사이즈가 적절하지 않을 때
    @ExceptionHandler(InvalidPageSizeException.class)
    public ResponseEntity<String> handleInvalidPageSizeException(InvalidPageSizeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // 기타 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(500).body("알 수 없는 오류가 발생했습니다.");
    }
}
