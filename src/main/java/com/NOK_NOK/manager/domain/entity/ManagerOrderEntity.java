package com.NOK_NOK.manager.domain.entity;

import com.NOK_NOK.order.domain.entity.CustomerSessionEntity;
import com.NOK_NOK.order.domain.entity.StoreEntity;
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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ManagerOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 고객 세션 (대상 인식 정보 포함)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private CustomerSessionEntity session;

    /**
     * 매장
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    /**
     * 주문 번호 (키오스크 화면 표시용)
     */
    @Column(name = "order_no", nullable = false)
    private Integer orderNo;

    /**
     * 주문 상태
     * 1: 완료, 0: 취소
     */
    @Column(name = "status", nullable = false)
    private Integer status;

    /**
     * 총 금액
     */
    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    /**
     * 주문 생성 시간
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * 결제 완료 시간
     */
    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    /**
     * 결제 방식
     * 예: CARD, QR, CASH, PAY
     */
    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    /**
     * 결제 상태
     * 예: SUCCESS, FAILED, PENDING
     */
    @Column(name = "payment_status", length = 20)
    private String paymentStatus;

    /**
     * PG사 트랜잭션 ID
     */
    @Column(name = "pg_transaction_id", length = 50)
    private String pgTransactionId;

    /**
     * 주문 아이템 목록
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ManagerOrderItemEntity> orderItems = new ArrayList<>();

    /**
     * 편의 메서드: 주문 아이템 추가
     */
    public void addOrderItem(ManagerOrderItemEntity orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    /**
     * 편의 메서드: 결제 완료 처리
     */
    public void completePayment(String paymentMethod, String pgTransactionId) {
        this.paymentMethod = paymentMethod;
        this.paymentStatus = "SUCCESS";
        this.pgTransactionId = pgTransactionId;
        this.paidAt = LocalDateTime.now();
        this.status = 1; // 완료
    }
}
