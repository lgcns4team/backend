package com.NOK_NOK.manager.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 매니저 주문 관리 응답 DTO
 */
public class ManagerOrderResponseDto {

    /**
     * 주문 목록 응답
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderList {
        private List<OrderSummary> orders;
    }

    /**
     * 주문 요약 정보 (목록용)
     * 프론트 UI: 주문번호, 주문시간, 채널, 총액, 상태
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderSummary {
        private Long orderId;           // 주문 ID
        private Integer orderNo;        // 주문번호 (#1245)
        private LocalDateTime orderTime; // 주문시간 (오후 2:38)
        private String channel;         // 채널 (키오스크)
        private Integer totalAmount;    // 총액 (₩32,000)
        private String status;          // 상태 (완료/취소)
    }

    /**
     * 주문 상세 응답
     * 프론트 UI: 주문시간, 결제수단, 상태, 총액, 주문메뉴
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetail {
        private Long orderId;
        private Integer orderNo;         // 주문번호
        private LocalDateTime orderTime; // 주문시간
        private String paymentMethod;    // 결제수단 (신용카드)
        private String status;           // 상태 (완료)
        private Integer totalAmount;     // 총액
        private List<OrderItem> items;   // 주문 메뉴
    }

    /**
     * 주문 아이템 (메뉴)
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        private String menuName;     // 메뉴명 (추천 세트)
        private Integer quantity;    // 수량 (1x)
        private Integer totalPrice;  // 금액 (₩32,000)
        private String options;      // 옵션 (커피 4잔, 미니 케이크 2개, 음료 4개)
    }
}