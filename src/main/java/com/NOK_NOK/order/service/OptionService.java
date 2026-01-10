package com.NOK_NOK.order.service;

import com.NOK_NOK.order.domain.dto.OptionResponseDto;
import com.NOK_NOK.order.domain.entity.MenuItemEntity;
import com.NOK_NOK.order.exceptions.MenuExceptions.*;
import com.NOK_NOK.order.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 옵션 서비스
 * 
 * 기능:
 * 1. 옵션 조회
 * 
 * 가격 계산은 프론트엔드에서 수행
 * 유효성 검증 및 최종 가격 계산은 주문 생성 시 수행
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OptionService {

    private final MenuItemRepository menuItemRepository;

    /**
     * 메뉴 옵션 조회
     * 
     * API: GET /api/menus/{menuId}/options
     * 
     * @param menuId 메뉴 ID
     * @return 메뉴 및 옵션 정보
     */
    public OptionResponseDto.MenuOptionDetail getMenuOptions(Long menuId) {
        log.info("[옵션 조회] 시작 - menuId: {}", menuId);

        // DB에서 메뉴 조회 (옵션 포함)
        MenuItemEntity menuItem = menuItemRepository.findByIdWithOptions(menuId)
                .orElseThrow(() -> new MenuNotFoundException(menuId));

        // 활성화 상태 확인
        if (!menuItem.getIsActive()) {
            throw new MenuNotActiveException(menuId);
        }

        log.info("[옵션 조회] 완료 - menuId: {}, 옵션 그룹 수: {}",
                menuId, menuItem.getOptionGroups().size());

        // Entity → DTO 변환
        return OptionResponseDto.MenuOptionDetail.from(menuItem);
    }
}