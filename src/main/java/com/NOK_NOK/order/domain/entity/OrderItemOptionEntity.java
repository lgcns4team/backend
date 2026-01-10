package com.NOK_NOK.order.domain.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 주문 아이템 옵션 Entity
 * 
 * 테이블: order_item_option
 * 
 * 주문 시점의 옵션 가격을 저장 (가격 변동 이력 관리)
 */
@Entity
@Table(name = "order_item_option")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderItemOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_option_id")
    private Long orderItemOptionId;

    /**
     * 주문 아이템
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItemEntity orderItem;

    /**
     * 옵션 아이템
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_item_id", nullable = false)
    private OptionItemEntity optionItem;

    /**
     * 주문 당시의 옵션 가격 (개별)
     * 
     * 현재 가격과 다를 수 있음 (가격 변동 이력)
     */
    @Column(name = "extra_price", nullable = false)
    private Integer extraPrice;

    /**
     * 옵션 수량
     * 
     * 예: 샷 2개 추가 → 2
     */
    @Column(name = "option_quantity", nullable = false)
    private Integer optionQuantity;

    /**
     * 편의 메서드: OrderItem 설정
     */
    public void setOrderItem(OrderItemEntity orderItem) {
        this.orderItem = orderItem;
    }
}
