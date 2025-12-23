package com.NOK_NOK.abs.domain.dto;

import com.NOK_NOK.abs.domain.entity.AdContentEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 광고 응답 DTO
 */
public class AdResponseDto {

    /**
     * 광고 목록 응답
     * 
     * API: GET /api/ads
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdList {
        
        /**
         * 광고 목록
         */
        private List<AdDetail> ads;

        /**
         * 총 광고 개수
         */
        private Integer totalCount;

        /**
         * Entity List → DTO 변환
         */
        public static AdList from(List<AdContentEntity> adContents) {
            List<AdDetail> ads = adContents.stream()
                    .map(AdDetail::from)
                    .collect(Collectors.toList());
            
            return AdList.builder()
                    .ads(ads)
                    .totalCount(ads.size())
                    .build();
        }
    }

    /**
     * 광고 상세 정보
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdDetail {
        
        /**
         * 광고 ID
         */
        private Long adId;

        /**
         * 광고 이름
         */
        private String title;

        /**
         * 미디어 타입 (IMAGE, VIDEO)
         */
        private String mediaType;

        /**
         * 미디어 URL
         */
        private String mediaUrl;

        /**
         * 광고 시작 날짜
         */
        private LocalDate startDate;

        /**
         * 광고 종료 날짜
         */
        private LocalDate endDate;

        /**
         * 활성화 여부
         */
        private Boolean isActive;

        /**
         * Entity → DTO 변환
         */
        public static AdDetail from(AdContentEntity adContent) {
            return AdDetail.builder()
                    .adId(adContent.getAdId())
                    .title(adContent.getTitle())
                    .mediaType(adContent.getMediaType())
                    .mediaUrl(adContent.getMediaUrl())
                    .startDate(adContent.getStartDate())
                    .endDate(adContent.getEndDate())
                    .isActive(adContent.getIsActive())
                    .build();
        }
    }

    /**
     * 결제 후 맞춤형 광고 응답
     * 
     * API: GET /api/ads/payment?ageGroup=20대&gender=M
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentAdList {
        
        /**
         * 타겟팅된 광고 목록
         */
        private List<AdDetail> ads;

        /**
         * 총 광고 개수
         */
        private Integer totalCount;

        /**
         * 타겟팅 정보
         */
        private String ageGroup;
        private String gender;

        /**
         * 메시지
         */
        private String message;
    }

    /**
     * 광고 표시 로그 저장 응답
     * 
     * API: POST /api/ads/display-log
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DisplayLogSaved {
        
        /**
         * 로그 ID
         */
        private Long displayId;

        /**
         * 세션 ID
         */
        // private Long sessionId;

        /**
         * 광고 ID
         */
        private Long adId;

        /**
         * 광고 제목
         */
        private String adTitle;

        /**
         * 노출 시간 (밀리초)
         */
        private Integer durationMs;

        /**
         * 메시지
         */
        private String message;
    }
}