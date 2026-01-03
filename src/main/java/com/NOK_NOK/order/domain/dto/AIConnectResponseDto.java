package com.NOK_NOK.order.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * AI 연결용 응답 DTO
 * 
 * AI 음성인식 시스템에 최적화된 간소화된 데이터 구조
 * 불필요한 필드 제거, 성능 최적화에 중점
 */
public class AIConnectResponseDto {

    /**
     * 간소화된 메뉴 정보
     * 
     * AI 음성인식에 필요한 최소 정보만 포함
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SimpleMenu {
        private Long menuId;
        private String name;           // 메뉴명 (음성인식 키워드용)
        private Integer price;         // 기본 가격
        private String categoryName;   // 카테고리명
    }

    /**
     * 간소화된 옵션 정보
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SimpleOption {
        private Long optionId;         // 옵션 ID
        private String name;           // 옵션명 (음성인식 키워드용)
        private Integer price;         // 추가 가격 (0일 수 있음)
    }

    /**
     * 간소화된 옵션 그룹 정보
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SimpleOptionGroup {
        private String groupName;      // 그룹명 (예: "사이즈", "온도")
        private Boolean isRequired;    // 필수 선택 여부
        private String selectionType;  // "SINGLE" 또는 "MULTI"
        private List<SimpleOption> options; // 그룹 내 옵션들
    }

    /**
     * 메뉴별 옵션 정보
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MenuOptionInfo {
        private Long menuId;
        private String menuName;
        private List<SimpleOptionGroup> optionGroups;
    }

    /**
     * 전체 데이터 (메뉴 + 옵션 통합)
     * 
     * AI 시스템 초기화용 완전한 데이터셋
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CompleteData {
        
        /**
         * 전체 메뉴 목록
         * 음성인식 키워드 매핑용
         */
        private List<SimpleMenu> menus;
        
        /**
         * 메뉴별 옵션 정보
         * Key: menuId, Value: 해당 메뉴의 옵션 정보
         * 
         * 사용 예:
         * menuOptions.get(1L) -> 메뉴 ID 1의 옵션 정보
         */
        private Map<Long, MenuOptionInfo> menuOptions;
        
        /**
         * 통계 정보
         */
        private Integer totalMenus;         // 전체 메뉴 수
        private Integer totalOptionGroups;  // 전체 옵션 그룹 수
        private LocalDateTime lastUpdated;  // 데이터 생성 시간
    }

    /**
     * 헬스체크 응답
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HealthCheck {
        private String status;           // "OK" 또는 "ERROR"
        private LocalDateTime timestamp; // 체크 시간
        private Long menuCount;          // DB 메뉴 수
        private Long categoryCount;      // DB 카테고리 수
        private String message;          // 상태 메시지
    }
}