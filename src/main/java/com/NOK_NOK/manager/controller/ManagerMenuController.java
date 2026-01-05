package com.NOK_NOK.manager.controller;

import com.NOK_NOK.manager.domain.dto.ManagerMenuResponseDto;
import com.NOK_NOK.manager.service.ManagerMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 매니저 메뉴 관리 Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/manager/menus")
@RequiredArgsConstructor
@Tag(name = "Manager Menu", description = "매니저 메뉴 관리 API")
public class ManagerMenuController {

    private final ManagerMenuService menuService;

    /**
     * 전체 메뉴 목록 조회
     * GET /api/manager/menus
     */
    @Operation(summary = "메뉴 목록 조회", description = "전체 메뉴 목록을 카테고리별로 조회합니다.")
    @GetMapping
    public ResponseEntity<ManagerMenuResponseDto.MenuList> getAllMenus() {
        log.info("GET /api/manager/menus - 전체 메뉴 목록 조회");
        
        ManagerMenuResponseDto.MenuList response = menuService.getAllMenus();
        
        return ResponseEntity.ok(response);
    }
}