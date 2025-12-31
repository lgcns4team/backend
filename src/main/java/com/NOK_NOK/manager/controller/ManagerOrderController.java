package com.NOK_NOK.manager.controller;

import com.NOK_NOK.manager.domain.dto.ManagerOrderResponseDto;
import com.NOK_NOK.manager.service.ManagerOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 매니저 주문 관리 Controller (단순 버전)
 */
@Slf4j
@RestController
@RequestMapping("/api/manager/orders")
@RequiredArgsConstructor
@Tag(name = "Manager Order", description = "매니저 주문 관리 API")
public class ManagerOrderController {

    private final ManagerOrderService orderService;

    /**
     * 전체 주문 목록 조회
     * GET /api/manager/orders
     */
    @Operation(summary = "주문 목록 조회", description = "전체 주문 목록을 최신순으로 조회합니다.")
    @GetMapping
    public ResponseEntity<ManagerOrderResponseDto.OrderList> getAllOrders() {
        log.info("GET /api/manager/orders - 전체 주문 목록 조회");
        
        ManagerOrderResponseDto.OrderList response = orderService.getAllOrders();
        
        return ResponseEntity.ok(response);
    }

    /**
     * 주문 상세 조회
     * GET /api/manager/orders/{orderId}
     */
    @Operation(summary = "주문 상세 조회", description = "주문 ID로 주문 상세 정보를 조회합니다.")
    @GetMapping("/{orderId}")
    public ResponseEntity<ManagerOrderResponseDto.OrderDetail> getOrderDetail(
            @Parameter(description = "주문 ID", example = "1", required = true)
            @PathVariable(name = "orderId") Long orderId
    ) {
        log.info("GET /api/manager/orders/{} - 주문 상세 조회", orderId);
        
        ManagerOrderResponseDto.OrderDetail response = orderService.getOrderDetail(orderId);
        
        return ResponseEntity.ok(response);
    }
}