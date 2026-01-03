package com.NOK_NOK.order.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 추천 메뉴 요청 DTO
 */
public class RecommendRequestDto {

    /**
     * 추천 메뉴 조회 요청
     * 
     * API: GET /api/menus/recommend
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Recommend {

        /**
         * 시간대 (필수)
         * MORNING: 아침 (06:00~11:59)
         * AFTERNOON: 오후 (12:00~17:59)
         * EVENING: 저녁 (18:00~05:59)
         */
        private String timeSlot;

        /**
         * 성별 (선택)
         * MALE: 남성
         * FEMALE: 여성
         * null: 대상 인식 실패
         */
        private String gender;

        /**
         * 연령대 (선택)
         * 10대, 20대, 30대, 40대, 50대+
         * null: 대상 인식 실패
         */
        private String ageGroup;

        /**
         * 조회 개수
         * 기본값: 5
         */
        @Builder.Default
        private Integer limit = 5;

        /**
         * 대상 인식 성공 여부 확인
         */
        public boolean hasFullRecognition() {
            return gender != null && ageGroup != null;
        }
    }
}
