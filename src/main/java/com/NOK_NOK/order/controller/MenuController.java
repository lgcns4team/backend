package com.NOK_NOK.order.controller;

import com.NOK_NOK.order.domain.dto.MenuRequest;
import com.NOK_NOK.order.domain.dto.MenuResponse;
import com.NOK_NOK.order.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Menu", description = "메뉴 관리 API")
public class MenuController {

        private final MenuService menuService;

        @GetMapping
        @Operation(summary = "메뉴 목록 조회", description = "필터링, 검색, 페이징이 적용된 메뉴 목록을 조회합니다")
        public ResponseEntity<MenuResponse.MenuPage> getMenus(
                        @Parameter(description = "카테고리 ID") @RequestParam(required = false) Long categoryId,
                        @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword,
                        @Parameter(description = "활성 상태 필터") @RequestParam(required = false) Boolean isActive,
                        @Parameter(description = "추천 메뉴 필터") @RequestParam(required = false) Boolean isRecommended,
                        @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") Integer page,
                        @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "20") Integer size,
                        @Parameter(description = "정렬 필드") @RequestParam(defaultValue = "displayOrder") String sortBy,
                        @Parameter(description = "정렬 방향 (ASC/DESC)") @RequestParam(defaultValue = "ASC") String sortDirection) {
                log.info("메뉴 목록 조회 요청 - 카테고리: {}, 키워드: {}, 활성: {}, 페이지: {}/{}",
                                categoryId, keyword, isActive, page, size);

                MenuRequest.Search request = MenuRequest.Search.builder()
                                .categoryId(categoryId)
                                .keyword(keyword)
                                .page(page)
                                .size(size)
                                .sortBy(sortBy)
                                .sortDirection(sortDirection)
                                .build();

                MenuResponse.MenuPage menuPage = menuService.getMenus(request);

                return ResponseEntity.ok(menuPage);
        }

        @GetMapping("/{menuId}")
        @Operation(summary = "메뉴 상세 조회", description = "특정 메뉴의 상세 정보를 조회합니다 (옵션 포함)")
        public ResponseEntity<MenuResponse.MenuDetail> getMenuDetail(
                        @Parameter(description = "메뉴 ID") @PathVariable("menuId") Long menuId) {
                log.info("메뉴 상세 조회 요청 - ID: {}", menuId);

                MenuResponse.MenuDetail menu = menuService.getMenuDetail(menuId);

                return ResponseEntity.ok(menu);
        }

        @GetMapping("/recommend")
        @Operation(summary = "추천 메뉴 조회", description = "연령대, 성별, 시간대를 기반으로 추천 메뉴를 조회합니다")
        public ResponseEntity<MenuResponse.RecommendedMenu> getRecommendedMenus(
                        @Parameter(description = "연령대") @RequestParam(required = false) String ageGroup,
                        @Parameter(description = "성별 (M/F)") @RequestParam(required = false) String gender,
                        @Parameter(description = "시간대") @RequestParam(required = false) String timeSlot,
                        @Parameter(description = "추천 개수") @RequestParam(defaultValue = "5") Integer limit) {
                log.info("추천 메뉴 조회 요청 - 연령대: {}, 성별: {}, 시간대: {}, 개수: {}",
                                ageGroup, gender, timeSlot, limit);

                MenuRequest.Recommend request = MenuRequest.Recommend.builder()
                                .ageGroup(ageGroup)
                                .gender(gender)
                                .timeSlot(timeSlot)
                                .limit(limit)
                                .build();

                MenuResponse.RecommendedMenu recommendedMenus = menuService.getRecommendedMenus(request);

                return ResponseEntity.ok(recommendedMenus);
        }

        @PostMapping
        @Operation(summary = "메뉴 생성", description = "새로운 메뉴를 생성합니다")
        public ResponseEntity<MenuResponse.MenuDetail> createMenu(
                        @Valid @RequestBody MenuRequest.Create request) {
                log.info("메뉴 생성 요청 - 이름: {}, 카테고리ID: {}", request.getName(), request.getCategoryId());

                MenuResponse.MenuDetail menu = menuService.createMenu(request);

                return ResponseEntity.ok(menu);
        }

        @PutMapping("/{menuId}")
        @Operation(summary = "메뉴 수정", description = "기존 메뉴를 수정합니다")
        public ResponseEntity<MenuResponse.MenuDetail> updateMenu(
                        @Parameter(description = "메뉴 ID") @PathVariable Long menuId,
                        @Valid @RequestBody MenuRequest.Update request) {
                log.info("메뉴 수정 요청 - ID: {}", menuId);

                MenuResponse.MenuDetail menu = menuService.updateMenu(menuId, request);

                return ResponseEntity.ok(menu);
        }

        @DeleteMapping("/{menuId}")
        @Operation(summary = "메뉴 삭제", description = "메뉴를 논리 삭제합니다")
        public ResponseEntity<Void> deleteMenu(
                        @Parameter(description = "메뉴 ID") @PathVariable Long menuId) {
                log.info("메뉴 삭제 요청 - ID: {}", menuId);

                menuService.deleteMenu(menuId);

                return ResponseEntity.noContent().build();
        }
}
