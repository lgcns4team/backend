package com.NOK_NOK.order.service;

import com.NOK_NOK.order.domain.dto.OrderRequestDto;
import com.NOK_NOK.order.domain.dto.OrderResponseDto;
import com.NOK_NOK.order.domain.entity.*;
import com.NOK_NOK.order.exceptions.OrderExceptions.*;
import com.NOK_NOK.order.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 주문 서비스
 * 
 * 기능:
 * 1. 주문 검증 (가격 재계산 및 검증)
 * 2. 주문 생성 (결제 완료 후 데이터 저장)
 * 3. 주문 조회
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerSessionRepository sessionRepository;
    private final StoreRepository storeRepository;
    private final MenuItemRepository menuItemRepository;
    private final OptionItemRepository optionItemRepository;

    /**
     * 주문 검증
     * 
     * API: POST /api/orders/validate
     * 
     * 프론트엔드에서 계산한 가격을 백엔드에서 재검증
     * 
     * @param request 주문 검증 요청
     * @return 검증 결과
     */
    @Transactional(readOnly = true)
    public OrderResponseDto.ValidateResult validateOrder(OrderRequestDto.ValidateOrder request) {
        log.info("[주문 검증] 시작 - storeId: {}, expectedAmount: {}",
                request.getStoreId(), request.getExpectedTotalAmount());

        try {
            // 1. 매장 검증
            StoreEntity store = storeRepository.findById(request.getStoreId())
                    .orElseThrow(() -> new StoreNotFoundException(request.getStoreId()));

            // 2. 각 주문 아이템 검증 및 가격 계산
            List<OrderResponseDto.ValidatedOrderItem> validatedItems = new ArrayList<>();
            int calculatedTotalAmount = 0;

            for (OrderRequestDto.OrderItemRequest itemRequest : request.getOrderItems()) {
                OrderResponseDto.ValidatedOrderItem validatedItem = validateOrderItem(itemRequest);
                validatedItems.add(validatedItem);
                calculatedTotalAmount += validatedItem.getLineAmount();
            }

            // 3. 총 금액 검증
            boolean isValid = (calculatedTotalAmount == request.getExpectedTotalAmount()) ? true : false;
            int priceDifference = calculatedTotalAmount - request.getExpectedTotalAmount();

            log.info("[주문 검증] 완료 - isValid: {}, calculated: {}, expected: {}, diff: {}",
                    isValid, calculatedTotalAmount, request.getExpectedTotalAmount(), priceDifference);

            return OrderResponseDto.ValidateResult.builder()
                    .isValid(isValid)
                    .calculatedTotalAmount(calculatedTotalAmount)
                    .expectedTotalAmount(request.getExpectedTotalAmount())
                    .priceDifference(priceDifference)
                    .validatedItems(validatedItems)
                    .errorMessage(isValid ? null : "가격이 일치하지 않습니다.")
                    .build();

        } catch (Exception e) {
            log.error("[주문 검증] 실패 - {}", e.getMessage(), e);
            
            return OrderResponseDto.ValidateResult.builder()
                    .isValid(false)
                    .calculatedTotalAmount(0)
                    .expectedTotalAmount(request.getExpectedTotalAmount())
                    .priceDifference(0)
                    .validatedItems(new ArrayList<>())
                    .errorMessage(e.getMessage())
                    .build();
        }
    }

    /**
     * 주문 아이템 검증 (가격 계산)
     */
    private OrderResponseDto.ValidatedOrderItem validateOrderItem(OrderRequestDto.OrderItemRequest itemRequest) {
        // 1. 메뉴 조회
        MenuItemEntity menu = menuItemRepository.findById(itemRequest.getMenuId())
                .orElseThrow(() -> new MenuNotFoundException(itemRequest.getMenuId()));

        if (!menu.getIsActive()) {
            throw new MenuNotActiveException(itemRequest.getMenuId());
        }

        // 2. 옵션 조회 및 가격 계산
        List<OrderResponseDto.ValidatedOption> validatedOptions = new ArrayList<>();
        int optionTotalPrice = 0;

        if (itemRequest.getSelectedOptions() != null && !itemRequest.getSelectedOptions().isEmpty()) {
            for (OrderRequestDto.SelectedOption selectedOption : itemRequest.getSelectedOptions()) {
                OptionItemEntity optionItem = optionItemRepository.findById(selectedOption.getOptionItemId())
                        .orElseThrow(() -> new OptionNotFoundException(selectedOption.getOptionItemId()));

                int optionLinePrice = optionItem.getOptionPrice() * selectedOption.getQuantity();
                optionTotalPrice += optionLinePrice;

                validatedOptions.add(OrderResponseDto.ValidatedOption.builder()
                        .optionItemId(optionItem.getOptionItemId())
                        .optionName(optionItem.getName())
                        .optionPrice(optionItem.getOptionPrice())
                        .quantity(selectedOption.getQuantity())
                        .totalPrice(optionLinePrice)
                        .build());
            }
        }

        // 3. 가격 계산
        int unitPrice = menu.getPrice() + optionTotalPrice;
        int lineAmount = unitPrice * itemRequest.getQuantity();

        return OrderResponseDto.ValidatedOrderItem.builder()
                .menuId(menu.getMenuId())
                .menuName(menu.getName())
                .quantity(itemRequest.getQuantity())
                .basePrice(menu.getPrice())
                .optionTotalPrice(optionTotalPrice)
                .unitPrice(unitPrice)
                .lineAmount(lineAmount)
                .options(validatedOptions)
                .build();
    }

    /**
     * 주문 생성
     * 
     * API: POST /api/orders
     * 
     * 결제 완료 후 주문 데이터 저장
     * Session도 함께 생성됨
     * 
     * @param request 주문 생성 요청
     * @return 주문 생성 결과
     */
    @Transactional
    public OrderResponseDto.OrderCreated createOrder(OrderRequestDto.CreateOrder request) {
        log.info("[주문 생성] 시작 - storeId: {}, paymentMethod: {}, ageGroup: {}, gender: {}",
                request.getStoreId(), request.getPaymentMethod(), request.getAgeGroup(), request.getGender());

        // 1. 매장 조회
        StoreEntity store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new StoreNotFoundException(request.getStoreId()));

        // 2. 세션 생성 (대상 인식 정보 포함)
        CustomerSessionEntity.Gender gender = null;
        if (request.getGender() != null) {
            try {
                gender = CustomerSessionEntity.Gender.valueOf(request.getGender());
            } catch (IllegalArgumentException e) {
                log.warn("[주문 생성] 잘못된 성별 값: {}", request.getGender());
            }
        }

        CustomerSessionEntity session = CustomerSessionEntity.builder()
                .store(store)
                .createdAt(LocalDateTime.now())
                .ageGroup(request.getAgeGroup())
                .gender(gender)
                .isSeniorMode(request.getIsSeniorMode() != null ? request.getIsSeniorMode() : false)
                .build();

        // 세션 저장
        CustomerSessionEntity savedSession = sessionRepository.save(session);
        log.info("[주문 생성] 세션 생성 완료 - sessionId: {}", savedSession.getSessionId());

        // 3. 주문 번호 생성 (오늘 날짜 기준 순번)
        Integer orderNo = orderRepository.findMaxOrderNoToday(store.getStoreId()) + 1;

        // 4. 주문 엔티티 생성
        OrderEntity order = OrderEntity.builder()
                .session(savedSession)
                .store(store)
                .orderNo(orderNo)
                .orderType(request.getOrderType() != null ? request.getOrderType() : "dine-in")
                .status(1) // 완료
                .totalAmount(request.getTotalAmount())
                .createdAt(LocalDateTime.now())
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus("SUCCESS")
                .pgTransactionId(request.getPgTransactionId())
                .paidAt(LocalDateTime.now())
                .build();

        // 5. 주문 아이템 생성
        List<OrderResponseDto.OrderItemSummary> orderItemSummaries = new ArrayList<>();

        for (OrderRequestDto.OrderItemRequest itemRequest : request.getOrderItems()) {
            // 메뉴 조회
            MenuItemEntity menu = menuItemRepository.findById(itemRequest.getMenuId())
                    .orElseThrow(() -> new MenuNotFoundException(itemRequest.getMenuId()));

            // 옵션 가격 계산
            int optionTotalPrice = 0;
            List<OrderItemOptionEntity> orderItemOptions = new ArrayList<>();
            List<String> optionNames = new ArrayList<>();

            if (itemRequest.getSelectedOptions() != null && !itemRequest.getSelectedOptions().isEmpty()) {
                // 옵션 ID 목록 추출
                List<Long> optionIds = itemRequest.getSelectedOptions().stream()
                        .map(OrderRequestDto.SelectedOption::getOptionItemId)
                        .collect(Collectors.toList());

                // 옵션 일괄 조회
                List<OptionItemEntity> optionItems = optionItemRepository.findByIdInWithGroup(optionIds);
                Map<Long, OptionItemEntity> optionMap = optionItems.stream()
                        .collect(Collectors.toMap(OptionItemEntity::getOptionItemId, opt -> opt));

                for (OrderRequestDto.SelectedOption selectedOption : itemRequest.getSelectedOptions()) {
                    OptionItemEntity optionItem = optionMap.get(selectedOption.getOptionItemId());
                    if (optionItem == null) {
                        throw new OptionNotFoundException(selectedOption.getOptionItemId());
                    }

                    int optionLinePrice = optionItem.getOptionPrice() * selectedOption.getQuantity();
                    optionTotalPrice += optionLinePrice;

                    // 주문 아이템 옵션 생성
                    OrderItemOptionEntity orderItemOption = OrderItemOptionEntity.builder()
                            .optionItem(optionItem)
                            .extraPrice(optionItem.getOptionPrice()) // 현재 가격 저장
                            .optionQuantity(selectedOption.getQuantity())
                            .build();

                    orderItemOptions.add(orderItemOption);
                    optionNames.add(optionItem.getName());
                }
            }

            // 가격 계산
            int unitPrice = menu.getPrice() + optionTotalPrice;
            int lineAmount = unitPrice * itemRequest.getQuantity();

            // 주문 아이템 생성
            OrderItemEntity orderItem = OrderItemEntity.builder()
                    .menuItem(menu)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(unitPrice)
                    .lineAmount(lineAmount)
                    .build();

            // 옵션 연결
            for (OrderItemOptionEntity optionEntity : orderItemOptions) {
                orderItem.addOrderItemOption(optionEntity);
            }

            // 주문에 추가
            order.addOrderItem(orderItem);

            // 응답 DTO 생성
            orderItemSummaries.add(OrderResponseDto.OrderItemSummary.builder()
                    .menuName(menu.getName())
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(unitPrice)
                    .lineAmount(lineAmount)
                    .optionNames(optionNames)
                    .build());
        }

        // 6. 주문 저장
        OrderEntity savedOrder = orderRepository.save(order);

        // 7. 세션 종료
        savedSession.endSession();

        log.info("[주문 생성] 완료 - orderId: {}, sessionId: {}, orderNo: {}, totalAmount: {}",
                savedOrder.getOrderId(), savedSession.getSessionId(), savedOrder.getOrderNo(), savedOrder.getTotalAmount());

        return OrderResponseDto.OrderCreated.builder()
                .orderId(savedOrder.getOrderId())
                .sessionId(savedSession.getSessionId())
                .orderNo(savedOrder.getOrderNo())
                .orderType(savedOrder.getOrderType())
                .totalAmount(savedOrder.getTotalAmount())
                .orderedAt(savedOrder.getCreatedAt())
                .paidAt(savedOrder.getPaidAt())
                .paymentMethod(savedOrder.getPaymentMethod())
                .status("COMPLETED")
                .orderItems(orderItemSummaries)
                .build();
    }

    /**
     * 주문 상세 조회
     * 
     * API: GET /api/orders/{orderId}
     * 
     * @param orderId 주문 ID
     * @return 주문 상세 정보
     */
    @Transactional(readOnly = true)
    public OrderResponseDto.OrderDetail getOrderDetail(Long orderId) {
        log.info("[주문 조회] 시작 - orderId: {}", orderId);

        OrderEntity order = orderRepository.findByIdWithSession(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        // 주문 아이템 상세 변환
        List<OrderResponseDto.OrderItemDetail> orderItemDetails = order.getOrderItems().stream()
                .map(item -> {
                    // 옵션 상세 변환
                    List<OrderResponseDto.OrderItemOptionDetail> optionDetails = item.getOrderItemOptions().stream()
                            .map(opt -> OrderResponseDto.OrderItemOptionDetail.builder()
                                    .optionName(opt.getOptionItem().getName())
                                    .extraPrice(opt.getExtraPrice())
                                    .quantity(opt.getOptionQuantity())
                                    .totalPrice(opt.getExtraPrice() * opt.getOptionQuantity())
                                    .build())
                            .collect(Collectors.toList());

                    return OrderResponseDto.OrderItemDetail.builder()
                            .orderItemId(item.getOrderItemId())
                            .menuName(item.getMenuItem().getName())
                            .quantity(item.getQuantity())
                            .unitPrice(item.getUnitPrice())
                            .lineAmount(item.getLineAmount())
                            .options(optionDetails)
                            .build();
                })
                .collect(Collectors.toList());

        // 시간대 계산 (created_at 기반)
        String timeSlot = calculateTimeSlot(order.getSession().getCreatedAt());

        log.info("[주문 조회] 완료 - orderId: {}, orderNo: {}", orderId, order.getOrderNo());

        return OrderResponseDto.OrderDetail.builder()
                .orderId(order.getOrderId())
                .orderNo(order.getOrderNo())
                .orderType(order.getOrderType())
                .totalAmount(order.getTotalAmount())
                .orderedAt(order.getCreatedAt())
                .paidAt(order.getPaidAt())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .status(order.getStatus())
                .ageGroup(order.getSession().getAgeGroup())
                .gender(order.getSession().getGender() != null ? 
                        order.getSession().getGender().name() : null)
                .timeSlot(timeSlot)
                .orderItems(orderItemDetails)
                .build();
    }

    /**
     * 시간대 계산 (created_at 기반)
     * 
     * MORNING: 06:00~11:59
     * AFTERNOON: 12:00~17:59
     * EVENING: 18:00~05:59
     * 
     * @param createdAt 세션 생성 시간
     * @return 시간대 문자열
     */
    private String calculateTimeSlot(LocalDateTime createdAt) {
        int hour = createdAt.getHour();
        
        if (hour >= 6 && hour < 12) {
            return "MORNING";
        } else if (hour >= 12 && hour < 18) {
            return "AFTERNOON";
        } else {
            return "EVENING";
        }
    }
}