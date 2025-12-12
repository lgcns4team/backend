package com.NOK_NOK.order.domain.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    /**
     * 메뉴
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private MenuItemEntity menuItem;

    /**
     * 수량
     */
    @Column(name = "quantity")
    private Integer quantity;

    /**
     * 가격 (옵션 포함)
     */
    @Column(name = "price")
    private Integer price;
}
