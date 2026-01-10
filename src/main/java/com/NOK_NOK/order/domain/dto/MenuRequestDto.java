package com.NOK_NOK.order.domain.dto;

import lombok.*;

@Getter
// @NoArgsConstructor
// @AllArgsConstructor
@Builder
public class MenuRequestDto {
    
    /**
     * 카테고리별 메뉴 조회 요청
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Search {
        private Long categoryId;
        private String keyword;
        private Boolean isActive;
        
        @Builder.Default
        private Integer page = 0;
        
        @Builder.Default
        private Integer size = 20;
        
        @Builder.Default
        private String sortBy = "menuId";
        
        @Builder.Default
        private String sortDirection = "ASC";
    }
}
