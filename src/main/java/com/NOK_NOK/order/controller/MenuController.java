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
     * 전체 카테고리 목록 조회
     * GET /api/categories
     */
    @Operation(summary = "카테고리 목록 조회", description = "전체 카테고리 목록을 display_order 순으로 조회합니다.")
    @GetMapping("/categories")
    public ResponseEntity<MenuResponseDto.CategoryList> getAllCategories() {
        log.info("GET /api/categories - Fetch all categories");
        
        MenuResponseDto.CategoryList response = menuService.getAllCategories();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 메뉴 검색 (조건 + 페이지네이션)
     * GET /api/menus
     * 
     * @param categoryId 카테고리 ID (선택)
     * @param keyword 검색 키워드 (선택)
     * @param isActive 활성화 여부 (선택)
     * @param page 페이지 번호 (기본값: 0)
     * @param size 페이지 크기 (기본값: 20)
     * @param sortBy 정렬 기준 (기본값: menuId)
     * @param sortDirection 정렬 방향 (기본값: ASC)
     */
    @Operation(
        summary = "메뉴 검색", 
        description = "조건에 맞는 메뉴를 검색합니다. 페이지네이션을 지원합니다."
    )
    @GetMapping("/menus")
    public ResponseEntity<MenuResponseDto.MenuPage> searchMenus(
        @Parameter(description = "카테고리 ID (1: 커피, 2: 논커피, 3: 디저트)", example = "1")
        @RequestParam(name = "categoryId", required = false) Long categoryId,
        
        @Parameter(description = "메뉴명 검색 키워드", example = "아메리카노")
        @RequestParam(name = "keyword", required = false) String keyword,
        
        @Parameter(description = "활성화 여부 (true: 판매중, false: 판매중단)", example = "true")
        @RequestParam(name = "isActive", required = false) Boolean isActive,
        
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        
        @Parameter(description = "페이지 크기", example = "20")
        @RequestParam(name = "size", defaultValue = "20") Integer size,
        
        @Parameter(description = "정렬 기준 필드 (menuId, name, price, categoryId)", example = "menuId")
        @RequestParam(name = "sortBy", defaultValue = "menuId") String sortBy,
        
        @Parameter(description = "정렬 방향 (ASC: 오름차순, DESC: 내림차순)", example = "ASC")
        @RequestParam(name = "sortDirection", defaultValue = "ASC") String sortDirection
    ) {
        log.info("GET /api/menus - categoryId: {}, keyword: {}, isActive: {}, page: {}, size: {}", 
                 categoryId, keyword, isActive, page, size);
        
        MenuRequestDto.Search request = MenuRequestDto.Search.builder()
            .categoryId(categoryId)
            .keyword(keyword)
            .isActive(isActive)
            .page(page)
            .size(size)
            .sortBy(sortBy)
            .sortDirection(sortDirection)
            .build();
        
        MenuResponseDto.MenuPage response = menuService.searchMenus(request);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 특정 메뉴 상세 조회
     * GET /api/menus/{menuId}
     * 
     * @param menuId 메뉴 ID
     */
    @Operation(summary = "메뉴 상세 조회", description = "특정 메뉴의 상세 정보를 조회합니다.")
    @GetMapping("/menus/{menuId}")
    public ResponseEntity<MenuResponseDto.MenuDetail> getMenuById(
        @Parameter(description = "메뉴 ID", example = "1", required = true)
        @PathVariable(name = "menuId") Long menuId
    ) {
        log.info("GET /api/menus/{} - Fetch menu detail", menuId);
        
        MenuResponseDto.MenuDetail response = menuService.getMenuById(menuId);
        
        return ResponseEntity.ok(response);
    }
}