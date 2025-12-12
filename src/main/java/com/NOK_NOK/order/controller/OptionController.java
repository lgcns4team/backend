package com.NOK_NOK.order.controller;

import com.NOK_NOK.order.domain.dto.OptionResponseDto;
import com.NOK_NOK.order.service.OptionService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 옵션 컨트롤러
 * 
 * API:
 * 1. GET /api/menus/{menuId}/options - 옵션 조회
 * 
 * 가격 계산은 프론트엔드에서 수행
 * 유효성 검증은 주문 생성 시 수행 (POST /api/orders)
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class OptionController {

    private final OptionService optionService;

    /**
     * 메뉴 옵션 조회
     * 
     * API: GET /api/menus/{menuId}/options
     * 
     * 사용 예시:
     * GET /api/menus/1/options
     * 
     * 응답:
     * {
     * "menuId": 1,
     * "menuName": "아메리카노",
     * "basePrice": 4000,
     * "optionGroups": [...]
     * }
     * 
     * @param menuId 메뉴 ID
     * @return 메뉴 옵션 정보
     */
    @Operation(summary = "메뉴 옵션 조회", description = "특정 메뉴의 옵션들을 조회합니다.")
    @GetMapping("/{menuId}/options")
    public ResponseEntity<OptionResponseDto.MenuOptionDetail> getMenuOptions(
            @PathVariable("menuId") Long menuId) {

        log.info("[API] GET /api/menus/{}/options", menuId);

        OptionResponseDto.MenuOptionDetail response = optionService.getMenuOptions(menuId);

        return ResponseEntity.ok(response);
    }
}