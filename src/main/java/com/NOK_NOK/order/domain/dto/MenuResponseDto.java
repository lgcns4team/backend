package com.NOK_NOK.order.domain.dto;

import lombok.*;
import java.util.List;

public class MenuResponseDto {

    /**
     * 카테고리 정보
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoryInfo {
        private Long categoryId;
        private String name;
        private Integer displayOrder;
    }

    /**
     * 메뉴 상세 정보
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MenuDetail {
        private Long menuId;
        private Long categoryId;
        private String categoryName;
        private String name;
        private Integer price;
        private Boolean isActive;
        private String imageUrl;
    }

    /**
     * 페이지네이션 메뉴 목록 (기존 방식)
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MenuPage {
        private List<MenuDetail> content;
        private Integer currentPage;
        private Integer totalPages;
        private Long totalElements;
        private Integer size;
        private Boolean hasNext;
        private Boolean hasPrevious;
    }

    /**
     * 카테고리 목록
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoryList {
        private List<CategoryInfo> categories;
    }

    /**
     * 카테고리별 메뉴 그룹 (키오스크 화면용)
     * 각 카테고리를 하나의 "페이지"로 간주
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoryWithMenus {
        private Long categoryId;
        private String categoryName;
        private Integer displayOrder;
        private List<MenuDetail> menus;
        private Integer menuCount; // 해당 카테고리의 메뉴 개수
    }

    /**
     * 전체 카테고리별 메뉴 목록 (키오스크 메인 화면용)
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoriesWithMenus {
        private List<CategoryWithMenus> categories;
        private Integer totalCategories; // 전체 카테고리 수 (= 페이지 수)
        private Integer maxMenusPerCategory; // 카테고리당 최대 메뉴 수
    }
}
