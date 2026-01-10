package com.NOK_NOK.order.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 주문 아이템 Entity
 * 
 * 테이블: order_item
 * 
 * 주문에 포함된 메뉴 정보
 */
@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    /**
     * 주문
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @Setter
    private OrderEntity order;

    /**
     * 메뉴
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuItemEntity menuItem;

    /**
     * 수량
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * 개별 금액 (메뉴 기본 가격 + 옵션 가격)
     * 
     * 예: 아메리카노(4000) + Large(500) = 4500
     */
    @Column(name = "unit_price", nullable = false)
    private Integer unitPrice;

    /**
     * 총 금액 (개별 금액 × 수량)
     * 
     * 예: 4500 × 2 = 9000
     */
    @Column(name = "line_amount", nullable = false)
    private Integer lineAmount;

    /**
     * 주문 아이템 옵션 목록
     */
    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItemOptionEntity> orderItemOptions = new ArrayList<>();

    /**
     * 편의 메서드: 옵션 추가
     */
    public void addOrderItemOption(OrderItemOptionEntity option) {
        this.orderItemOptions.add(option);
        option.setOrderItem(this);
    }
}
