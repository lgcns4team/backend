package com.NOK_NOK.order.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 옵션 요청 DTO
 */
public class OptionRequestDto {

    /**
     * 옵션 조회 요청
     * 
     * API: GET /api/menus/{menuId}/options
     * 
     * 현재는 파라미터 없이 사용하지만,
     * 나중에 필터링 기능 추가 시 사용
     * 
     * 사용 예시:
     * GET /api/menus/1/options
     * GET /api/menus/1/options?includeInactive=true
     * GET /api/menus/1/options?onlyRequired=true
     * GET /api/menus/1/options?storeId=1
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionQuery {

        /**
         * 비활성 옵션 포함 여부
         * 기본값: false (활성 옵션만)
         * 
         * 예: 관리자 화면에서는 true
         */
        private Boolean includeInactive = false;

        /**
         * 필수 옵션만 조회
         * 기본값: false (전체 조회)
         * 
         * 예: 빠른 주문 화면에서는 true
         */
        private Boolean onlyRequired = false;

        /**
         * 특정 옵션 그룹만 조회
         * 기본값: null (전체 조회)
         * 
         * 예: groupIds=1,3 → 그룹 1, 3만 조회
         */
        private List<Long> groupIds;

        /**
         * 매장 ID
         * 기본값: null (전체 매장)
         * 
         * 예: 매장별로 다른 옵션 제공 시 사용
         */
        private Long storeId;
    }
}