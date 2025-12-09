package com.NOK_NOK.order.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

public class MenuRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "메뉴 목록 조회 요청")
    public static class Search {
        
        @Schema(description = "카테고리 ID", example = "1")
        private Long categoryId;

        @Schema(description = "검색 키워드", example = "아메리카노")
        private String keyword;

        @Schema(description = "페이지 번호 (0부터 시작)", example = "0")
        @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다")
        @Builder.Default
        private Integer page = 0;

        @Schema(description = "페이지 크기", example = "20")
        @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다")
        @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다")
        @Builder.Default
        private Integer size = 20;

        @Schema(description = "정렬 필드", example = "menuId")
        @Builder.Default
        private String sortBy = "menuId";

        @Schema(description = "정렬 방향 (ASC/DESC)", example = "ASC")
        @Pattern(regexp = "^(ASC|DESC)$", message = "정렬 방향은 ASC 또는 DESC만 가능합니다")
        @Builder.Default
        private String sortDirection = "ASC";
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "추천 메뉴 조회 요청")
    public static class Recommend {
        
        @Schema(description = "연령대", example = "20대")
        private String ageGroup;

        @Schema(description = "성별", example = "M")
        @Pattern(regexp = "^(M|F)$", message = "성별은 M 또는 F만 가능합니다")
        private String gender;

        @Schema(description = "시간대", example = "MORNING")
        @Pattern(regexp = "^(MORNING|LUNCH|AFTERNOON|DINNER|NIGHT)$", 
                 message = "시간대는 MORNING, LUNCH, AFTERNOON, DINNER, NIGHT 중 하나여야 합니다")
        private String timeSlot;

        @Schema(description = "추천 개수", example = "5")
        @Min(value = 1, message = "추천 개수는 1 이상이어야 합니다")
        @Max(value = 20, message = "추천 개수는 20 이하여야 합니다")
        @Builder.Default
        private Integer limit = 5;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "메뉴 생성 요청")
    public static class Create {
        
        @NotNull(message = "카테고리 ID는 필수입니다")
        @Schema(description = "카테고리 ID", example = "1", required = true)
        private Long categoryId;

        @NotBlank(message = "메뉴명은 필수입니다")
        @Size(max = 100, message = "메뉴명은 100자 이하여야 합니다")
        @Schema(description = "메뉴명", example = "아이스 아메리카노", required = true)
        private String name;

        @NotNull(message = "가격은 필수입니다")
        @DecimalMin(value = "0.0", inclusive = false, message = "가격은 0보다 커야 합니다")
        @Schema(description = "가격", example = "4500.00", required = true)
        private BigDecimal price;

        @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
        private String imageUrl;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "메뉴 수정 요청")
    public static class Update {
        
        @Schema(description = "카테고리 ID", example = "1")
        private Long categoryId;

        @Size(max = 100, message = "메뉴명은 100자 이하여야 합니다")
        @Schema(description = "메뉴명", example = "아이스 아메리카노")
        private String name;

        @DecimalMin(value = "0.0", inclusive = false, message = "가격은 0보다 커야 합니다")
        @Schema(description = "가격", example = "4500.00")
        private BigDecimal price;

        @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
        private String imageUrl;
    }
}
