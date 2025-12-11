package com.NOK_NOK.order.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문 Entity
 * 
 * 테이블: orders
 */
@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 고객 세션 (대상 인식 정보 포함)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private CustomerSessionEntity session;

    /**
     * 주문 시간
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * 총 금액
     */
    @Column(name = "total_amount")
    private Integer totalAmount;

    /**
     * 주문 상태
     * 예: PENDING, COMPLETED, CANCELLED
     */
    @Column(name = "status", length = 20)
    private String status;

    /**
     * 주문 아이템 목록
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OrderItemEntity> orderItems = new ArrayList<>();
}
