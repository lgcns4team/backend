package com.NOK_NOK.order.service;

import com.NOK_NOK.order.domain.dto.MenuRequestDto;
import com.NOK_NOK.order.domain.dto.MenuResponseDto;
import com.NOK_NOK.order.domain.entity.CategoryEntity;
import com.NOK_NOK.order.domain.entity.MenuItemEntity;
import com.NOK_NOK.order.repository.CategoryRepository;
import com.NOK_NOK.order.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {
    
    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;
    
    /**
     * 전체 카테고리 목록 조회
     */
    public MenuResponseDto.CategoryList getAllCategories() {
        log.info("Fetching all categories");
        
        List<CategoryEntity> categories = categoryRepository.findAllByOrderByDisplayOrderAsc();
        
        List<MenuResponseDto.CategoryInfo> categoryInfoList = categories.stream()
            .map(this::convertToCategoryInfo)
            .collect(Collectors.toList());
        
        return MenuResponseDto.CategoryList.builder()
            .categories(categoryInfoList)
            .build();
    }
    
    /**
     * 조건별 메뉴 검색 (페이지네이션)
     */
    public MenuResponseDto.MenuPage searchMenus(MenuRequestDto.Search request) {
        log.info("Searching menus with conditions - categoryId: {}, keyword: {}, isActive: {}, page: {}, size: {}", 
                 request.getCategoryId(), request.getKeyword(), request.getIsActive(), 
                 request.getPage(), request.getSize());
        
        // Pageable 객체 생성
        Pageable pageable = createPageable(request);
        
        // 메뉴 검색
        Page<MenuItemEntity> menuPage = menuItemRepository.searchMenus(
            request.getCategoryId(),
            request.getIsActive(),
            request.getKeyword(),
            pageable
        );
        
        // 카테고리 정보를 미리 조회 (N+1 방지)
        Map<Long, String> categoryNameMap = getCategoryNameMap();
        
        // DTO 변환
        List<MenuResponseDto.MenuDetail> menuDetails = menuPage.getContent().stream()
            .map(menu -> convertToMenuDetail(menu, categoryNameMap))
            .collect(Collectors.toList());
        
        return MenuResponseDto.MenuPage.builder()
            .content(menuDetails)
            .currentPage(menuPage.getNumber())
            .totalPages(menuPage.getTotalPages())
            .totalElements(menuPage.getTotalElements())
            .size(menuPage.getSize())
            .hasNext(menuPage.hasNext())
            .hasPrevious(menuPage.hasPrevious())
            .build();
    }
    
    /**
     * 특정 메뉴 상세 조회
     */
    public MenuResponseDto.MenuDetail getMenuById(Long menuId) {
        log.info("Fetching menu detail - menuId: {}", menuId);
        
        MenuItemEntity menuItem = menuItemRepository.findById(menuId)
            .orElseThrow(() -> new IllegalArgumentException("Menu not found with id: " + menuId));
        
        Map<Long, String> categoryNameMap = getCategoryNameMap();
        
        return convertToMenuDetail(menuItem, categoryNameMap);
    }
    
    /**
     * Pageable 객체 생성
     */
    private Pageable createPageable(MenuRequestDto.Search request) {
        Sort sort = Sort.by(
            "DESC".equalsIgnoreCase(request.getSortDirection()) 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC,
            request.getSortBy()
        );
        
        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }
    
    /**
     * 카테고리 ID -> 이름 매핑 생성
     */
    private Map<Long, String> getCategoryNameMap() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        Map<Long, String> categoryNameMap = new HashMap<>();
        
        for (CategoryEntity category : categories) {
            categoryNameMap.put(category.getCategoryId(), category.getName());
        }
        
        return categoryNameMap;
    }
    
    /**
     * Entity -> CategoryInfo DTO 변환
     */
    private MenuResponseDto.CategoryInfo convertToCategoryInfo(CategoryEntity category) {
        return MenuResponseDto.CategoryInfo.builder()
            .categoryId(category.getCategoryId())
            .name(category.getName())
            .displayOrder(category.getDisplayOrder())
            .build();
    }
    
    /**
     * Entity -> MenuDetail DTO 변환
     */
    private MenuResponseDto.MenuDetail convertToMenuDetail(MenuItemEntity menuItem, Map<Long, String> categoryNameMap) {
        return MenuResponseDto.MenuDetail.builder()
            .menuId(menuItem.getMenuId())
            .categoryId(menuItem.getCategoryId())
            .categoryName(categoryNameMap.get(menuItem.getCategoryId()))
            .name(menuItem.getName())
            .price(menuItem.getPrice())
            .isActive(menuItem.getIsActive())
            .imageUrl(menuItem.getImageUrl())
            .build();
    }
}
