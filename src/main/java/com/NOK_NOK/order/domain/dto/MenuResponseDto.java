package com.NOK_NOK.order.domain.dto;

import lombok.*;
import java.util.List;

@Getter
// @NoArgsConstructor
// @AllArgsConstructor
@Builder
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
     * 페이지네이션 메뉴 목록
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
}
