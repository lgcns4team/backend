package com.NOK_NOK.order.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 옵션 요청 DTO
 */
public class OptionRequestDto {

    /**
     * 가격 계산 요청
     * 
     * API: POST /api/menus/{menuId}/options/calculate
     * 
     * 요청 예시:
     * {
     *   "menuId": 1,
     *   "selectedOptionIds": [2, 4, 6]
     * }
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceCalculation {

        /**
         * 메뉴 ID
         */
        @NotNull(message = "메뉴 ID는 필수입니다")
        private Long menuId;

        /**
         * 선택한 옵션 ID 목록
         * 
         * 예: [2, 4, 6]
         * - 2: Large (+500원)
         * - 4: 얼음 보통 (0원)
         * - 6: 샷 1개 추가 (+500원)
         */
        @NotEmpty(message = "선택된 옵션이 없습니다")
        private List<Long> selectedOptionIds;
    }
}
