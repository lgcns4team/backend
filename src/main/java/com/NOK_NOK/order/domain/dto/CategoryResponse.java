package com.NOK_NOK.order.domain.dto;

import com.NOK_NOK.order.domain.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryResponse {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "카테고리 목록 응답")
    public static class CategoryList {
        
        @Schema(description = "카테고리 ID", example = "1")
        private Long categoryId;

        @Schema(description = "카테고리명", example = "커피")
        private String name;

        @Schema(description = "표시 순서", example = "1")
        private Integer displayOrder;

        @Schema(description = "메뉴 개수", example = "15")
        private Integer menuCount;

        public static CategoryList from(Category category) {
            return CategoryList.builder()
                    .categoryId(category.getCategoryId())
                    .name(category.getName())
                    .displayOrder(category.getDisplayOrder())
                    .menuCount(category.getMenuItems().size())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "카테고리 상세 응답")
    public static class CategoryDetail {
        
        @Schema(description = "카테고리 ID", example = "1")
        private Long categoryId;

        @Schema(description = "카테고리명", example = "커피")
        private String name;

        @Schema(description = "표시 순서", example = "1")
        private Integer displayOrder;

        @Schema(description = "메뉴 목록")
        private List<MenuResponse.MenuList> menus;

        @Schema(description = "생성일", example = "2025-01-01T00:00:00")
        private LocalDateTime createdAt;

        @Schema(description = "수정일", example = "2025-01-15T10:30:00")
        private LocalDateTime updatedAt;

        public static CategoryDetail from(Category category) {
            return CategoryDetail.builder()
                    .categoryId(category.getCategoryId())
                    .name(category.getName())
                    .displayOrder(category.getDisplayOrder())
                    .menus(category.getMenuItems().stream()
                            .map(MenuResponse.MenuList::from)
                            .collect(Collectors.toList()))
                    .createdAt(category.getCreatedAt())
                    .updatedAt(category.getUpdatedAt())
                    .build();
        }
    }
}
