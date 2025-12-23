package com.NOK_NOK.abs.controller.advice;

import com.NOK_NOK.abs.exceptions.AdExceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * 광고 예외 처리
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.NOK_NOK.abs")
public class AdExceptionHandler {

    /**
     * 광고를 찾을 수 없음
     */
    @ExceptionHandler(AdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAdNotFound(AdNotFoundException e) {
        log.error("[예외] AdNotFoundException: {}", e.getMessage());
        
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Ad Not Found")
                .message(e.getMessage())
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * 세션을 찾을 수 없음
     */
    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSessionNotFound(SessionNotFoundException e) {
        log.error("[예외] SessionNotFoundException: {}", e.getMessage());
        
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Session Not Found")
                .message(e.getMessage())
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * 활성화된 광고가 없음
     */
    @ExceptionHandler(NoActiveAdsException.class)
    public ResponseEntity<ErrorResponse> handleNoActiveAds(NoActiveAdsException e) {
        log.warn("[예외] NoActiveAdsException: {}", e.getMessage());
        
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("No Active Ads")
                .message(e.getMessage())
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * 잘못된 광고 로그 요청
     */
    @ExceptionHandler(InvalidDisplayLogRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDisplayLogRequest(InvalidDisplayLogRequestException e) {
        log.error("[예외] InvalidDisplayLogRequestException: {}", e.getMessage());
        
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Display Log Request")
                .message(e.getMessage())
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * 에러 응답 DTO
     */
    @lombok.Getter
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class ErrorResponse {
        private LocalDateTime timestamp;
        private Integer status;
        private String error;
        private String message;
    }
}