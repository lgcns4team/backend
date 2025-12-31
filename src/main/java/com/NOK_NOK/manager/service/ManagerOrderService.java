package com.NOK_NOK.manager.service;

import com.NOK_NOK.manager.domain.dto.ManagerOrderResponseDto;
import com.NOK_NOK.manager.repository.ManagerOrderRepository;
import com.NOK_NOK.order.domain.entity.OrderEntity;
import com.NOK_NOK.order.domain.entity.OrderItemEntity;
import com.NOK_NOK.order.domain.entity.OrderItemOptionEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 매니저 주문 관리 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManagerOrderService {

    private final ManagerOrderRepository orderRepository;

    /**
     * 전체 주문 목록 조회
     */
    public ManagerOrderResponseDto.OrderList getAllOrders() {
        log.info("[주문 목록 조회] 전체 주문 조회");

        List<OrderEntity> orders = orderRepository.findAllOrders();

        List<ManagerOrderResponseDto.OrderSummary> summaries = orders.stream()
                .map(this::toOrderSummary)
                .collect(Collectors.toList());

        return ManagerOrderResponseDto.OrderList.builder()
                .orders(summaries)
                .build();
    }

    /**
     * 주문 상세 조회
     */
    public ManagerOrderResponseDto.OrderDetail getOrderDetail(Long orderId) {
        log.info("[주문 상세 조회] orderId: {}", orderId);

        OrderEntity order = orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다. orderId: " + orderId));

        return toOrderDetail(order);
    }

    /**
     * OrderEntity → OrderSummary 변환
     */
    private ManagerOrderResponseDto.OrderSummary toOrderSummary(OrderEntity order) {
        return ManagerOrderResponseDto.OrderSummary.builder()
                .orderId(order.getOrderId())
                .orderNo(order.getOrderNo())
                .orderTime(order.getCreatedAt())
                .channel("키오스크")  // 고정값
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus() == 1 ? "완료" : "취소")
                .build();
    }

    /**
     * OrderEntity → OrderDetail 변환
     */
    private ManagerOrderResponseDto.OrderDetail toOrderDetail(OrderEntity order) {
        List<ManagerOrderResponseDto.OrderItem> items = order.getOrderItems().stream()
                .map(this::toOrderItem)
                .collect(Collectors.toList());

        return ManagerOrderResponseDto.OrderDetail.builder()
                .orderId(order.getOrderId())
                .orderNo(order.getOrderNo())
                .orderTime(order.getCreatedAt())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getStatus() == 1 ? "완료" : "취소")
                .totalAmount(order.getTotalAmount())
                .items(items)
                .build();
    }

    /**
     * OrderItemEntity → OrderItem 변환
     */
    private ManagerOrderResponseDto.OrderItem toOrderItem(OrderItemEntity item) {
        // 옵션 문자열 생성 (예: "커피 4잔, 미니 케이크 2개")
        String options = item.getOrderItemOptions().stream()
                .map(this::toOptionString)
                .collect(Collectors.joining(", "));

        return ManagerOrderResponseDto.OrderItem.builder()
                .menuName(item.getMenuItem().getName())
                .quantity(item.getQuantity())
                .totalPrice(item.getLineAmount())
                .options(options.isEmpty() ? "옵션 없음" : options)
                .build();
    }

    /**
     * OrderItemOptionEntity → 옵션 문자열 변환
     */
    private String toOptionString(OrderItemOptionEntity option) {
        return option.getOptionItem().getName();
    }
}