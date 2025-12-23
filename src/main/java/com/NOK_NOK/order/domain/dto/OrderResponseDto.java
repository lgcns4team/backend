package com.NOK_NOK.order.domain.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 주문 응답 DTO
 */
public class OrderResponseDto {

    /**
     * 주문 검증 응답
     * 
     * API: POST /api/orders/validate
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidateResult {
        
        /**
         * 검증 성공 여부
         */
        private Boolean isValid;

        /**
         * 백엔드에서 계산한 총 금액
         */
        private Integer calculatedTotalAmount;

        /**
         * 프론트에서 보낸 총 금액
         */
        private Integer expectedTotalAmount;

        /**
         * 금액 차이
         */
        private Integer priceDifference;

        /**
         * 검증된 주문 아이템 목록
         */
        private List<ValidatedOrderItem> validatedItems;

        /**
         * 오류 메시지 (검증 실패 시)
         */
        private String errorMessage;
    }

    /**
     * 검증된 주문 아이템
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidatedOrderItem {
        
        private Long menuId;
        private String menuName;
        private Integer quantity;
        private Integer basePrice;
        private Integer optionTotalPrice;
        private Integer unitPrice;
        private Integer lineAmount;
        private List<ValidatedOption> options;
    }

    /**
     * 검증된 옵션
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidatedOption {
        
        private Long optionItemId;
        private String optionName;
        private Integer optionPrice;
        private Integer quantity;
        private Integer totalPrice;
    }

    /**
     * 주문 생성 응답
     * 
     * API: POST /api/orders
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderCreated {
        
        /**
         * 주문 ID
         */
        private Long orderId;

        /**
         * 세션 ID (광고 조회용)
         */
        private Long sessionId;

        /**
         * 주문 번호 (화면 표시용)
         */
        private Integer orderNo;

        /**
         * 총 금액
         */
        private Integer totalAmount;

        /**
         * 주문 시간
         */
        private LocalDateTime orderedAt;

        /**
         * 결제 완료 시간
         */
        private LocalDateTime paidAt;

        /**
         * 결제 방식
         */
        private String paymentMethod;

        /**
         * 주문 상태
         */
        private String status;

        /**
         * 주문 아이템 목록
         */
        private List<OrderItemSummary> orderItems;
    }

    /**
     * 주문 아이템 요약
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemSummary {
        
        private Long orderItemId;
        private String menuName;
        private Integer quantity;
        private Integer unitPrice;
        private Integer lineAmount;
        private List<String> optionNames;
    }

    /**
     * 주문 상세 조회 응답
     * 
     * API: GET /api/orders/{orderId}
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetail {
        
        private Long orderId;
        private Integer orderNo;
        private Integer totalAmount;
        private LocalDateTime orderedAt;
        private LocalDateTime paidAt;
        private String paymentMethod;
        private String paymentStatus;
        private Integer status;
        
        /**
         * 대상 인식 정보
         */
        private String ageGroup;
        private String gender;
        private String timeSlot;
        
        /**
         * 주문 아이템 상세
         */
        private List<OrderItemDetail> orderItems;
    }

    /**
     * 주문 아이템 상세
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemDetail {
        
        private Long orderItemId;
        private String menuName;
        private Integer quantity;
        private Integer unitPrice;
        private Integer lineAmount;
        private List<OrderItemOptionDetail> options;
    }

    /**
     * 주문 아이템 옵션 상세
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemOptionDetail {
        
        private String optionName;
        private Integer extraPrice;
        private Integer quantity;
        private Integer totalPrice;
    }
}