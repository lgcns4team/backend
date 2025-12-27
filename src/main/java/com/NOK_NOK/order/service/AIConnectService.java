package com.NOK_NOK.order.service;

import com.NOK_NOK.order.domain.dto.AIConnectResponseDto;
import com.NOK_NOK.order.domain.entity.CategoryEntity;
import com.NOK_NOK.order.domain.entity.MenuItemEntity;
import com.NOK_NOK.order.repository.CategoryRepository;
import com.NOK_NOK.order.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AI 연결 서비스
 * 
 * AI 음성인식 시스템을 위한 데이터 처리 서비스
 * 기존 MenuService, OptionService를 활용하여 AI 최적화된 형태로 데이터 제공
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AIConnectService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    /**
     * AI용 전체 데이터 조회
     * 
     * 메뉴와 옵션을 모두 포함한 완전한 데이터셋 제공
     * AI 시스템 초기화 및 캐싱용
     * 
     * @return 전체 메뉴 및 옵션 정보
     */
    public AIConnectResponseDto.CompleteData getCompleteDataForAI() {
        log.info("[AI 서비스] 전체 데이터 조회 시작");

        // 1. 활성화된 메뉴만 조회 (menuId 순 정렬)
        List<MenuItemEntity> menus = menuItemRepository.findAll(
            Sort.by(Sort.Direction.ASC, "menuId")
        ).stream()
        .filter(menu -> menu.getIsActive())
        .collect(Collectors.toList());

        // 2. 카테고리 정보 미리 로드 (N+1 문제 방지)
        List<CategoryEntity> categories = categoryRepository.findAll();
        Map<Long, String> categoryMap = categories.stream()
            .collect(Collectors.toMap(
                CategoryEntity::getCategoryId, 
                CategoryEntity::getName
            ));

        // 3. 메뉴 DTO 변환
        List<AIConnectResponseDto.SimpleMenu> simpleMenus = menus.stream()
            .map(menu -> AIConnectResponseDto.SimpleMenu.builder()
                .menuId(menu.getMenuId())
                .name(menu.getName())
                .price(menu.getPrice())
                .categoryName(categoryMap.get(menu.getCategoryId()))
                .build())
            .collect(Collectors.toList());

        // 4. 메뉴별 옵션 정보 수집
        Map<Long, AIConnectResponseDto.MenuOptionInfo> menuOptionsMap = new HashMap<>();
        int totalOptionGroups = 0;

        for (MenuItemEntity menu : menus) {
            // 옵션 그룹 포함하여 메뉴 조회
            MenuItemEntity menuWithOptions = menuItemRepository
                .findByIdWithOptions(menu.getMenuId())
                .orElse(menu);

            if (!menuWithOptions.getOptionGroups().isEmpty()) {
                // 옵션 그룹 DTO 변환
                List<AIConnectResponseDto.SimpleOptionGroup> optionGroups = 
                    menuWithOptions.getOptionGroups().stream()
                    .map(group -> {
                        List<AIConnectResponseDto.SimpleOption> options = 
                            group.getOptions().stream()
                            .map(option -> AIConnectResponseDto.SimpleOption.builder()
                                .optionId(option.getOptionItemId())
                                .name(option.getName())
                                .price(option.getOptionPrice())
                                .build())
                            .collect(Collectors.toList());

                        return AIConnectResponseDto.SimpleOptionGroup.builder()
                            .groupName(group.getName())
                            .isRequired(group.getIsRequired())
                            .selectionType(group.getSelectionType())
                            .options(options)
                            .build();
                    })
                    .collect(Collectors.toList());

                // 메뉴별 옵션 정보 저장
                AIConnectResponseDto.MenuOptionInfo optionInfo = 
                    AIConnectResponseDto.MenuOptionInfo.builder()
                    .menuId(menu.getMenuId())
                    .menuName(menu.getName())
                    .optionGroups(optionGroups)
                    .build();

                menuOptionsMap.put(menu.getMenuId(), optionInfo);
                totalOptionGroups += optionGroups.size();
            }
        }

        log.info("[AI 서비스] 전체 데이터 조회 완료 - 메뉴 {}개, 옵션 그룹 {}개", 
                simpleMenus.size(), totalOptionGroups);

        // 5. 최종 응답 데이터 구성
        return AIConnectResponseDto.CompleteData.builder()
            .menus(simpleMenus)
            .menuOptions(menuOptionsMap)
            .totalMenus(simpleMenus.size())
            .totalOptionGroups(totalOptionGroups)
            .lastUpdated(LocalDateTime.now())
            .build();
    }

    /**
     * AI 시스템 연결 상태 확인
     * 
     * @return 헬스체크 정보
     */
    public AIConnectResponseDto.HealthCheck getHealthStatus() {
        try {
            // DB 연결 상태 확인 (간단한 count 쿼리)
            long menuCount = menuItemRepository.count();
            long categoryCount = categoryRepository.count();

            return AIConnectResponseDto.HealthCheck.builder()
                .status("OK")
                .timestamp(LocalDateTime.now())
                .menuCount(menuCount)
                .categoryCount(categoryCount)
                .message("AI 연결 서비스가 정상적으로 동작 중입니다.")
                .build();

        } catch (Exception e) {
            log.error("[AI 서비스] 헬스체크 실패", e);

            return AIConnectResponseDto.HealthCheck.builder()
                .status("ERROR")
                .timestamp(LocalDateTime.now())
                .menuCount(0L)
                .categoryCount(0L)
                .message("데이터베이스 연결 오류: " + e.getMessage())
                .build();
        }
    }
}