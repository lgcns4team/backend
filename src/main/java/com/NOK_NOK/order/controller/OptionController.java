package com.NOK_NOK.order.controller;

import com.NOK_NOK.order.domain.dto.OptionRequestDto;
import com.NOK_NOK.order.domain.dto.OptionResponseDto;
import com.NOK_NOK.order.service.OptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 옵션 컨트롤러
 * 
 * API:
 * 1. GET /api/menus/{menuId}/options - 옵션 조회
 * 2. POST /api/menus/{menuId}/options/calculate - 가격 계산
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class OptionController {

    private final OptionService optionService;

    /**
     * 1. 메뉴 옵션 조회
     * 
     * API: GET /api/menus/{menuId}/options
     * 
     * 예시:
     * GET /api/menus/1/options
     * 
     * 응답:
     * {
     *   "menuId": 1,
     *   "menuName": "아메리카노",
     *   "basePrice": 4000,
     *   "optionGroups": [...]
     * }
     * 
     * @param menuId 메뉴 ID
     * @return 메뉴 옵션 정보
     */
    @GetMapping("/{menuId}/options")
    public ResponseEntity<OptionResponseDto.MenuOptionDetail> getMenuOptions(
            @PathVariable("menuId") Long menuId) {

        log.info("[API] GET /api/menus/{}/options", menuId);

        OptionResponseDto.MenuOptionDetail response = optionService.getMenuOptions(menuId);

        return ResponseEntity.ok(response);
    }

    /**
     * 2. 옵션 선택 & 가격 계산
     * 
     * API: POST /api/menus/{menuId}/options/calculate
     * 
     * 요청 예시:
     * POST /api/menus/1/options/calculate
     * {
     *   "menuId": 1,
     *   "selectedOptionIds": [2, 4, 6]
     * }
     * 
     * 응답 예시:
     * {
     *   "menuId": 1,
     *   "menuName": "아메리카노",
     *   "basePrice": 4000,
     *   "optionPrice": 1000,
     *   "totalPrice": 5000,
     *   "selectedOptions": [...],
     *   "isValid": true,
     *   "errorMessage": null
     * }
     * 
     * @param menuId 메뉴 ID
     * @param request 선택한 옵션 ID 목록
     * @return 가격 계산 결과
     */
    @PostMapping("/{menuId}/options/calculate")
    public ResponseEntity<OptionResponseDto.PriceCalculation> calculatePrice(
            @PathVariable("menuId") Long menuId,
            @Valid @RequestBody OptionRequestDto.PriceCalculation request) {

        log.info("[API] POST /api/menus/{}/options/calculate - optionIds: {}", 
                menuId, request.getSelectedOptionIds());

        OptionResponseDto.PriceCalculation response = 
                optionService.calculatePrice(menuId, request);

        return ResponseEntity.ok(response);
    }
}
