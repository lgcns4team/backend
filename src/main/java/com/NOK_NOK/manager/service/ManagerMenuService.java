package com.NOK_NOK.manager.service;

import com.NOK_NOK.manager.domain.dto.ManagerMenuResponseDto;
import com.NOK_NOK.manager.repository.ManagerMenuRepository;
import com.NOK_NOK.order.domain.entity.MenuItemEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 매니저 메뉴 관리 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManagerMenuService {

    private final ManagerMenuRepository menuRepository;

    /**
     * 전체 메뉴 목록 조회
     */
    public ManagerMenuResponseDto.MenuList getAllMenus() {
        log.info("[메뉴 목록 조회] 전체 메뉴 조회");

        List<MenuItemEntity> menus = menuRepository.findAllMenusWithCategory();

        List<ManagerMenuResponseDto.MenuItem> menuItems = menus.stream()
                .map(this::toMenuItem)
                .collect(Collectors.toList());

        return ManagerMenuResponseDto.MenuList.builder()
                .menus(menuItems)
                .totalCount(menuItems.size())
                .build();
    }

    /**
     * MenuItemEntity → MenuItem 변환
     */
    private ManagerMenuResponseDto.MenuItem toMenuItem(MenuItemEntity menu) {
        return ManagerMenuResponseDto.MenuItem.builder()
                .menuId(menu.getMenuId())
                .menuName(menu.getName())
                .categoryName(menu.getCategory().getName())
                .price(menu.getPrice())
                .isActive(menu.getIsActive())
                .imageUrl(menu.getImageUrl())
                .build();
    }
}