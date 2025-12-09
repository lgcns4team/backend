package com.NOK_NOK.order.domain.dto;

import com.NOK_NOK.order.domain.entity.MenuItem;
import com.NOK_NOK.order.domain.entity.OptionGroup;
import com.NOK_NOK.order.domain.entity.OptionItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "메뉴 목록 응답")
    public static class MenuList {
        
        @Schema(description = "메뉴 ID", example = "1")
        private Long menuId;

        @Schema(description = "카테고리 ID", example = "1")
        private Long categoryId;

        @Schema(description = "카테고리명", example = "커피")
        private String categoryName;

        @Schema(description = "메뉴명", example = "아이스 아메리카노")
        private String name;

        @Schema(description = "가격", example = "4500.00")
        private BigDecimal price;

        @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
        private String imageUrl;

        public static MenuList from(MenuItem menuItem) {
            return MenuList.builder()
                    .menuId(menuItem.getMenuId())
                    .categoryId(menuItem.getCategory().getCategoryId())
                    .categoryName(menuItem.getCategory().getName())
                    .name(menuItem.getName())
                    .price(menuItem.getPrice())
                    .imageUrl(menuItem.getImageUrl())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "메뉴 상세 응답")
    public static class MenuDetail {
        
        @Schema(description = "메뉴 ID", example = "1")
        private Long menuId;

        @Schema(description = "카테고리 ID", example = "1")
        private Long categoryId;

        @Schema(description = "카테고리명", example = "커피")
        private String categoryName;

        @Schema(description = "메뉴명", example = "아이스 아메리카노")
        private String name;

        @Schema(description = "가격", example = "4500.00")
        private BigDecimal price;

        @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
        private String imageUrl;

        @Schema(description = "옵션 그룹 목록")
        private List<OptionGroupInfo> optionGroups;

        @Schema(description = "생성일", example = "2025-01-01T00:00:00")
        private LocalDateTime createdAt;

        @Schema(description = "수정일", example = "2025-01-15T10:30:00")
        private LocalDateTime updatedAt;

        public static MenuDetail from(MenuItem menuItem) {
            return MenuDetail.builder()
                    .menuId(menuItem.getMenuId())
                    .categoryId(menuItem.getCategory().getCategoryId())
                    .categoryName(menuItem.getCategory().getName())
                    .name(menuItem.getName())
                    .price(menuItem.getPrice())
                    .imageUrl(menuItem.getImageUrl())
                    .optionGroups(menuItem.getOptionGroups().stream()
                            .map(OptionGroupInfo::from)
                            .collect(Collectors.toList()))
                    .createdAt(menuItem.getCreatedAt())
                    .updatedAt(menuItem.getUpdatedAt())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "옵션 그룹 정보")
    public static class OptionGroupInfo {
        
        @Schema(description = "옵션 그룹 ID", example = "1")
        private Long optionGroupId;

        @Schema(description = "옵션 그룹명", example = "온도")
        private String name;

        @Schema(description = "필수 여부", example = "true")
        private Boolean isRequired;

        @Schema(description = "선택 타입", example = "SINGLE")
        private String selectionType;

        @Schema(description = "옵션 목록")
        private List<OptionItemInfo> options;

        public static OptionGroupInfo from(OptionGroup optionGroup) {
            return OptionGroupInfo.builder()
                    .optionGroupId(optionGroup.getOptionGroupId())
                    .name(optionGroup.getName())
                    .isRequired(optionGroup.getIsRequired())
                    .selectionType(optionGroup.getSelectionType())
                    .options(optionGroup.getOptionItems().stream()
                            .map(OptionItemInfo::from)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "옵션 정보")
    public static class OptionItemInfo {
        
        @Schema(description = "옵션 ID", example = "1")
        private Long optionId;

        @Schema(description = "옵션명", example = "HOT")
        private String name;

        @Schema(description = "추가 가격", example = "0.00")
        private BigDecimal price;

        public static OptionItemInfo from(OptionItem optionItem) {
            return OptionItemInfo.builder()
                    .optionId(optionItem.getOptionId())
                    .name(optionItem.getName())
                    .price(optionItem.getPrice())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "페이징된 메뉴 목록 응답")
    public static class MenuPage {
        
        @Schema(description = "메뉴 목록")
        private List<MenuList> content;

        @Schema(description = "현재 페이지 번호", example = "0")
        private Integer pageNumber;

        @Schema(description = "페이지 크기", example = "20")
        private Integer pageSize;

        @Schema(description = "전체 요소 개수", example = "100")
        private Long totalElements;

        @Schema(description = "전체 페이지 개수", example = "5")
        private Integer totalPages;

        @Schema(description = "마지막 페이지 여부", example = "false")
        private Boolean isLast;

        @Schema(description = "첫 페이지 여부", example = "true")
        private Boolean isFirst;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "추천 메뉴 응답")
    public static class RecommendedMenu {
        
        @Schema(description = "메뉴 목록")
        private List<MenuList> menus;

        @Schema(description = "추천 근거", example = "연령대와 시간대를 기반으로 한 추천")
        private String reason;

        @Schema(description = "조회 시간", example = "2025-12-09T10:30:00")
        private LocalDateTime timestamp;
    }
}
