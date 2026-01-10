package com.NOK_NOK.order.repository;

import com.NOK_NOK.order.domain.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Order Repository
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    /**
     * 주문 ID로 주문 상세 조회 (세션 정보 포함)
     * 
     * @param orderId 주문 ID
     * @return 주문 엔티티 (세션 포함)
     */
    @Query("SELECT o FROM OrderEntity o " +
           "JOIN FETCH o.session s " +
           "LEFT JOIN FETCH s.store " +
           "WHERE o.orderId = :orderId")
    Optional<OrderEntity> findByIdWithSession(@Param("orderId") Long orderId);

    /**
     * 특정 매장의 오늘 날짜 최대 주문 번호 조회
     * 
     * @param storeId 매장 ID
     * @return 최대 주문 번호
     */
    @Query("SELECT COALESCE(MAX(o.orderNo), 0) FROM OrderEntity o " +
           "WHERE o.store.storeId = :storeId " +
           "AND DATE(o.createdAt) = CURRENT_DATE")
    Integer findMaxOrderNoToday(@Param("storeId") Long storeId);
}
