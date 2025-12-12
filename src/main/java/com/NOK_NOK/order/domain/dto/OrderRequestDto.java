package com.NOK_NOK.order.domain.dto;

import lombok.*;

import java.util.List;

/**
 * 주문 요청 DTO
 */
public class OrderRequestDto {

    /**
     * 주문 검증 요청
     * 
     * API: POST /api/orders/validate
     * 
     * 프론트엔드에서 계산한 가격을 백엔드에서 재검증
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ValidateOrder {
        
        /**
         * 세션 ID (대상 인식 정보 포함)
         */
        private Long sessionId;

        /**
         * 매장 ID
         */
        private Long storeId;

        /**
         * 주문 아이템 목록
         */
        private List<OrderItemRequest> orderItems;

        /**
         * 프론트에서 계산한 총 금액
         */
        private Integer expectedTotalAmount;
    }

    /**
     * 주문 생성 요청
     * 
     * API: POST /api/orders
     * 
     * 결제 완료 후 주문 데이터 저장
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateOrder {
        
        /**
         * 세션 ID (대상 인식 정보 포함)
         */
        private Long sessionId;

        /**
         * 매장 ID
         */
        private Long storeId;

        /**
         * 주문 아이템 목록
         */
        private List<OrderItemRequest> orderItems;

        /**
         * 결제 방식
         * CARD, QR, CASH, PAY
         */
        private String paymentMethod;

        /**
         * PG사 트랜잭션 ID
         */
        private String pgTransactionId;

        /**
         * 총 금액
         */
        private Integer totalAmount;
    }

    /**
     * 주문 아이템 정보
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemRequest {
        
        /**
         * 메뉴 ID
         */
        private Long menuId;

        /**
         * 수량
         */
        private Integer quantity;

        /**
         * 선택한 옵션 목록
         */
        private List<SelectedOption> selectedOptions;
    }

    /**
     * 선택한 옵션 정보
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SelectedOption {
        
        /**
         * 옵션 아이템 ID
         */
        private Long optionItemId;

        /**
         * 옵션 수량
         * 예: 샷 2개 추가 → 2
         */
        @Builder.Default
        private Integer quantity = 1;
    }
}
