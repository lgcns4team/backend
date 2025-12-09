package com.NOK_NOK.order.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

public class CategoryRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "카테고리 목록 조회 요청")
    public static class Search {
        
        @Schema(description = "검색 키워드", example = "커피")
        private String keyword;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "카테고리 생성 요청")
    public static class Create {
        
        @NotBlank(message = "카테고리명은 필수입니다")
        @Size(max = 100, message = "카테고리명은 100자 이하여야 합니다")
        @Schema(description = "카테고리명", example = "커피", required = true)
        private String name;

        @Schema(description = "표시 순서", example = "1")
        private Integer displayOrder;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "카테고리 수정 요청")
    public static class Update {
        
        @Size(max = 100, message = "카테고리명은 100자 이하여야 합니다")
        @Schema(description = "카테고리명", example = "커피")
        private String name;

        @Schema(description = "표시 순서", example = "1")
        private Integer displayOrder;
    }
}
