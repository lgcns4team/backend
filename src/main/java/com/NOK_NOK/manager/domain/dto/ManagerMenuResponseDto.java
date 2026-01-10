package com.NOK_NOK.manager.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 매니저 메뉴 관리 응답 DTO
 */
public class ManagerMenuResponseDto {

    /**
     * 메뉴 목록 응답
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuList {
        private List<MenuItem> menus;
        private Integer totalCount;
    }

    /**
     * 메뉴 아이템 (카드형)
     * 프론트 UI: 이미지, 메뉴명, 가격, 판매상태
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuItem {
        private Long menuId;        // 메뉴 ID
        private String menuName;    // 메뉴명 (아메리카노)
        private String categoryName; // 카테고리명 (커피)
        private Integer price;      // 가격 (₩4,900)
        private Boolean isActive;   // 판매 상태 (true: 판매, false: 비판매)
        private String imageUrl;    // 이미지 URL
    }
}