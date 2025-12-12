package com.NOK_NOK.order.controller;

import com.NOK_NOK.order.domain.dto.OrderRequestDto;
import com.NOK_NOK.order.domain.dto.OrderResponseDto;
import com.NOK_NOK.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 주문 컨트롤러
 * 
 * API:
 * 1. POST /api/orders/validate - 주문 검증
 * 2. POST /api/orders - 주문 생성
 * 3. GET /api/orders/{orderId} - 주문 조회
 */
@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "주문 관리 API")
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 검증
     * 
     * API: POST /api/orders/validate
     * 
     * 프론트엔드에서 계산한 가격을 백엔드에서 재검증
     * 결제 전 단계에서 호출
     * 
     * @param request 주문 검증 요청
     * @return 검증 결과
     */
    @Operation(
        summary = "주문 검증",
        description = "결제 전 주문 내역을 검증합니다. 프론트엔드에서 계산한 가격과 백엔드에서 재계산한 가격을 비교합니다."
    )
    @PostMapping("/validate")
    public ResponseEntity<OrderResponseDto.ValidateResult> validateOrder(
            @RequestBody OrderRequestDto.ValidateOrder request) {

        log.info("[API] POST /api/orders/validate - sessionId: {}, expectedAmount: {}",
                request.getSessionId(), request.getExpectedTotalAmount());

        OrderResponseDto.ValidateResult result = orderService.validateOrder(request);

        return ResponseEntity.ok(result);
    }

    /**
     * 주문 생성
     * 
     * API: POST /api/orders
     * 
     * 결제 완료 후 주문 데이터 저장
     * 
     * @param request 주문 생성 요청
     * @return 주문 생성 결과
     */
    @Operation(
        summary = "주문 생성",
        description = "결제 완료 후 주문 데이터를 저장합니다. 대상 인식 정보(연령대, 성별, 시간대)도 함께 저장됩니다."
    )
    @PostMapping
    public ResponseEntity<OrderResponseDto.OrderCreated> createOrder(
            @RequestBody OrderRequestDto.CreateOrder request) {

        log.info("[API] POST /api/orders - sessionId: {}, paymentMethod: {}",
                request.getSessionId(), request.getPaymentMethod());

        OrderResponseDto.OrderCreated result = orderService.createOrder(request);

        return ResponseEntity.ok(result);
    }

    /**
     * 주문 상세 조회
     * 
     * API: GET /api/orders/{orderId}
     * 
     * @param orderId 주문 ID
     * @return 주문 상세 정보
     */
    @Operation(
        summary = "주문 상세 조회",
        description = "주문 ID로 주문 상세 정보를 조회합니다. 주문 아이템, 옵션, 대상 인식 정보가 포함됩니다."
    )
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto.OrderDetail> getOrderDetail(
            @Parameter(description = "주문 ID", example = "1", required = true)
            @PathVariable("orderId") Long orderId) {

        log.info("[API] GET /api/orders/{}", orderId);

        OrderResponseDto.OrderDetail result = orderService.getOrderDetail(orderId);

        return ResponseEntity.ok(result);
    }
}
