package com.NOK_NOK.order.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 추천 메뉴 응답 DTO
 */
public class RecommendResponseDto {

    /**
     * 추천 메뉴 목록 응답
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendList {

        /**
         * 시간대
         */
        private String timeSlot;

        /**
         * 성별 (대상 인식 성공 시)
         */
        private String gender;

        /**
         * 연령대 (대상 인식 성공 시)
         */
        private String ageGroup;

        /**
         * 추천 메뉴 목록
         */
        private List<RecommendedMenu> recommendedMenus;

        /**
         * 총 메뉴 개수
         */
        private Integer totalCount;

        /**
         * 추천 타입
         * FULL: 시간대 + 성별 + 연령대 (1순위)
         * TIME_ONLY: 시간대만 (2순위 디폴트)
         * POPULAR: 전체 인기 (3순위)
         */
        private String recommendType;
    }

    /**
     * 추천 메뉴 정보
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendedMenu {

        /**
         * 메뉴 ID
         */
        private Long menuId;

        /**
         * 메뉴명
         */
        private String menuName;

        /**
         * 카테고리명
         */
        private String categoryName;

        /**
         * 기본 가격
         */
        private Integer basePrice;

        /**
         * 이미지 URL
         */
        private String imageUrl;

        /**
         * 주문 횟수
         */
        private Long orderCount;

        /**
         * 인기 순위
         */
        private Integer popularRank;

        /**
         * 추천 이유
         */
        private String recommendReason;
    }
}
