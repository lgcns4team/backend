package com.NOK_NOK.abs.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 광고 요청 DTO
 */
public class AdRequestDto {

    /**
     * 결제 후 맞춤형 광고 조회 요청
     * 
     * API: GET /api/ads/payment?sessionId=1
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaymentAdRequest {
        
        /**
         * 세션 ID (연령대, 성별 정보 포함)
         */
        private Long sessionId;
    }

    /**
     * 광고 표시 로그 저장 요청
     * 
     * API: POST /api/ads/display-log
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SaveDisplayLog {
        
        /**
         * 세션 ID
         */
        private Long sessionId;

        /**
         * 광고 ID
         */
        private Long adId;

        /**
         * 표시 시작 시간
         * ISO 8601 형식: "2023-12-11T14:30:00"
         */
        private LocalDateTime displayedAt;

        /**
         * 노출 시간 (밀리초)
         */
        private Integer durationMs;
    }
}