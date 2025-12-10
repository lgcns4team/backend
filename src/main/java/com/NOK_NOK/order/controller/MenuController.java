package com.NOK_NOK.order.controller;

import com.NOK_NOK.order.domain.dto.MenuRequestDto;
import com.NOK_NOK.order.domain.dto.MenuResponseDto;
import com.NOK_NOK.order.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Menu", description = "메뉴 관리 API")
public class MenuController {

    private final MenuService menuService;

    /**
     * 키오스크용: 전체 카테고리와 메뉴 조회
     * GET /api/kiosk/categories-with-menus
     * 
     * 각 카테고리를 하나의 "페이지"로 간주하여 반환
     * 활성화된 메뉴만 조회
     */
    @Operation(summary = "키오스크 메뉴판 조회", description = "전체 카테고리와 각 카테고리별 활성화된 메뉴를 조회합니다. 키오스크 메인 화면용입니다.")
    @GetMapping("/categories-with-menus")
    public ResponseEntity<MenuResponseDto.CategoriesWithMenus> getCategoriesWithMenus() {
        log.info("GET /api/categories-with-menus - Fetch all categories with menus for kiosk");

        MenuResponseDto.CategoriesWithMenus response = menuService.getCategoriesWithMenusForKiosk();

        return ResponseEntity.ok(response);
    }

    /**
     * 키오스크용: 특정 카테고리의 메뉴 조회
     * GET /api/kiosk/categories/{categoryId}/menus
     * 
     * @param categoryId 카테고리 ID
     */
    @Operation(summary = "카테고리별 메뉴 조회", description = "특정 카테고리의 활성화된 메뉴만 조회합니다.")
    @GetMapping("/menus/{categoryId}")
    public ResponseEntity<MenuResponseDto.CategoryWithMenus> getMenusByCategory(
            @Parameter(description = "카테고리 ID", example = "1", required = true) @PathVariable(name = "categoryId") Long categoryId) {
        log.info("GET /api/menus/{} - Fetch menus for category", categoryId);

        MenuResponseDto.CategoryWithMenus response = menuService.getMenusByCategory(categoryId);

        return ResponseEntity.ok(response);
    }
}