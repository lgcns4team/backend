package com.NOK_NOK.manager.repository;

import com.NOK_NOK.order.domain.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 매니저 주문 관리 Repository
 */
@Repository
public interface ManagerOrderRepository extends JpaRepository<OrderEntity, Long> {

    /**
     * 전체 주문 목록 조회 (최신순)
     */
    @Query("SELECT o FROM OrderEntity o ORDER BY o.createdAt DESC")
    List<OrderEntity> findAllOrders();

    /**
     * 주문 상세 조회 (OrderItem 포함)
     * MultipleBagFetchException 방지를 위해 2단계로 조회
     */
    @Query("SELECT DISTINCT o FROM OrderEntity o " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.menuItem " +
            "WHERE o.orderId = :orderId")
    Optional<OrderEntity> findByIdWithDetails(@Param("orderId") Long orderId);
}