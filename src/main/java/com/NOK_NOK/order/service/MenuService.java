package com.NOK_NOK.order.service;

import com.NOK_NOK.order.domain.dto.MenuResponseDto;
import com.NOK_NOK.order.domain.entity.CategoryEntity;
import com.NOK_NOK.order.domain.entity.MenuItemEntity;
import com.NOK_NOK.order.repository.CategoryRepository;
import com.NOK_NOK.order.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

        private final MenuItemRepository menuItemRepository;
        private final CategoryRepository categoryRepository;

        /**
         * 키오스크용: 전체 카테고리와 각 카테고리별 메뉴 조회
         * 활성화된 메뉴만 조회
         */
        public MenuResponseDto.CategoriesWithMenus getCategoriesWithMenusForKiosk() {
                log.info("Fetching categories with menus for kiosk");

                // 1. 전체 카테고리 조회 (display_order 순)
                List<CategoryEntity> categories = categoryRepository.findAllByOrderByDisplayOrderAsc();

                // 2. 카테고리별로 활성화된 메뉴 조회
                int maxMenuCount = 0;
                List<MenuResponseDto.CategoryWithMenus> categoryWithMenusList = new ArrayList<>();

                for (CategoryEntity category : categories) {
                        // 해당 카테고리의 활성화된 메뉴만 조회
                        List<MenuItemEntity> menus = menuItemRepository.findByCategoryIdAndIsActive(
                                        category.getCategoryId(),
                                        true,
                                        Sort.by(Sort.Direction.ASC, "menuId"));

                        // 메뉴 DTO 변환
                        List<MenuResponseDto.MenuDetail> menuDetails = menus.stream()
                                        .map(menu -> MenuResponseDto.MenuDetail.builder()
                                                        .menuId(menu.getMenuId())
                                                        .categoryId(menu.getCategoryId())
                                                        .categoryName(category.getName())
                                                        .name(menu.getName())
                                                        .price(menu.getPrice())
                                                        .isActive(menu.getIsActive())
                                                        .imageUrl(menu.getImageUrl())
                                                        .build())
                                        .collect(Collectors.toList());

                        // 최대 메뉴 개수 갱신
                        if (menuDetails.size() > maxMenuCount) {
                                maxMenuCount = menuDetails.size();
                        }

                        // 카테고리별 메뉴 그룹 생성
                        categoryWithMenusList.add(
                                        MenuResponseDto.CategoryWithMenus.builder()
                                                        .categoryId(category.getCategoryId())
                                                        .categoryName(category.getName())
                                                        .displayOrder(category.getDisplayOrder())
                                                        .menus(menuDetails)
                                                        .menuCount(menuDetails.size())
                                                        .build());
                }

                return MenuResponseDto.CategoriesWithMenus.builder()
                                .categories(categoryWithMenusList)
                                .totalCategories(categoryWithMenusList.size())
                                .maxMenusPerCategory(maxMenuCount)
                                .build();
        }

        /**
         * 키오스크용: 특정 카테고리의 메뉴만 조회
         * 
         * @param categoryId 카테고리 ID
         */
        public MenuResponseDto.CategoryWithMenus getMenusByCategory(Long categoryId) {
                log.info("Fetching menus for category: {}", categoryId);

                // 카테고리 정보 조회
                CategoryEntity category = categoryRepository.findById(categoryId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Category not found with id: " + categoryId));

                // 해당 카테고리의 활성화된 메뉴 조회
                List<MenuItemEntity> menus = menuItemRepository.findByCategoryIdAndIsActive(
                                categoryId,
                                true,
                                Sort.by(Sort.Direction.ASC, "menuId"));

                // 메뉴 DTO 변환
                List<MenuResponseDto.MenuDetail> menuDetails = menus.stream()
                                .map(menu -> MenuResponseDto.MenuDetail.builder()
                                                .menuId(menu.getMenuId())
                                                .categoryId(menu.getCategoryId())
                                                .categoryName(category.getName())
                                                .name(menu.getName())
                                                .price(menu.getPrice())
                                                .isActive(menu.getIsActive())
                                                .imageUrl(menu.getImageUrl())
                                                .build())
                                .collect(Collectors.toList());

                return MenuResponseDto.CategoryWithMenus.builder()
                                .categoryId(category.getCategoryId())
                                .categoryName(category.getName())
                                .displayOrder(category.getDisplayOrder())
                                .menus(menuDetails)
                                .menuCount(menuDetails.size())
                                .build();
        }
}
