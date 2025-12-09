package com.NOK_NOK.order.service;

import com.NOK_NOK.order.domain.dto.OptionRequestDto;
import com.NOK_NOK.order.domain.dto.OptionResponseDto;
import com.NOK_NOK.order.domain.entity.MenuItemEntity;
import com.NOK_NOK.order.domain.entity.OptionGroupEntity;
import com.NOK_NOK.order.domain.entity.OptionItemEntity;
import com.NOK_NOK.order.exceptions.MenuExceptions.*;
import com.NOK_NOK.order.repository.MenuItemRepository;
import com.NOK_NOK.order.repository.OptionItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 옵션 서비스
 * 
 * 기능:
 * 1. 옵션 조회
 * 2. 옵션 선택 & 가격 계산
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OptionService {

    private final MenuItemRepository menuItemRepository;
    private final OptionItemRepository optionItemRepository;

    /**
     * 1. 메뉴 옵션 조회
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

    /**
     * 2. 옵션 선택 & 가격 계산
     * 
     * API: POST /api/menus/{menuId}/options/calculate
     * 
     * 처리 과정:
     * 1) 메뉴 조회
     * 2) 선택한 옵션 조회
     * 3) 유효성 검증
     *    - 선택한 옵션이 해당 메뉴의 옵션인가?
     *    - 필수 옵션을 모두 선택했는가?
     *    - SINGLE 타입에서 2개 이상 선택하지 않았는가?
     * 4) 가격 계산
     * 5) 결과 반환
     * 
     * @param menuId 메뉴 ID
     * @param request 선택한 옵션 ID 목록
     * @return 가격 계산 결과
     */
    public OptionResponseDto.PriceCalculation calculatePrice(
            Long menuId, 
            OptionRequestDto.PriceCalculation request) {
        
        log.info("[가격 계산] 시작 - menuId: {}, optionIds: {}", 
                menuId, request.getSelectedOptionIds());

        // Step 1: 메뉴 조회
        MenuItemEntity menuItem = menuItemRepository.findByIdWithOptions(menuId)
                .orElseThrow(() -> new MenuNotFoundException(menuId));

        // Step 2: 선택한 옵션 조회
        List<OptionItemEntity> selectedOptions = optionItemRepository
                .findByIdInWithGroup(request.getSelectedOptionIds());

        log.debug("[가격 계산] 선택된 옵션 수: {}", selectedOptions.size());

        // Step 3: 유효성 검증
        ValidationResult validationResult = validateOptions(menuItem, selectedOptions);

        // 검증 실패
        if (!validationResult.isValid()) {
            log.warn("[가격 계산] 유효성 검증 실패 - {}", validationResult.getErrorMessage());
            
            return OptionResponseDto.PriceCalculation.builder()
                    .menuId(menuId)
                    .menuName(menuItem.getName())
                    .basePrice(menuItem.getPrice())
                    .optionPrice(0)
                    .totalPrice(menuItem.getPrice())
                    .selectedOptions(Collections.emptyList())
                    .isValid(false)
                    .errorMessage(validationResult.getErrorMessage())
                    .build();
        }

        // Step 4: 가격 계산
        // 옵션 추가 금액 합계
        int optionPrice = selectedOptions.stream()
                .mapToInt(OptionItemEntity::getOptionPrice)
                .sum();

        // 총 금액 = 기본 가격 + 옵션 가격
        int totalPrice = menuItem.getPrice() + optionPrice;

        log.info("[가격 계산] 완료 - 기본가: {}, 옵션가: {}, 총액: {}", 
                menuItem.getPrice(), optionPrice, totalPrice);

        // Step 5: 선택된 옵션 정보 생성
        List<OptionResponseDto.PriceCalculation.SelectedOptionInfo> selectedOptionInfos 
                = selectedOptions.stream()
                .map(option -> OptionResponseDto.PriceCalculation.SelectedOptionInfo.builder()
                        .optionItemId(option.getOptionItemId())
                        .optionGroupName(option.getOptionGroup().getName())
                        .optionName(option.getName())
                        .additionalPrice(option.getOptionPrice())
                        .build())
                .collect(Collectors.toList());

        // Step 6: 성공 응답 반환
        return OptionResponseDto.PriceCalculation.builder()
                .menuId(menuId)
                .menuName(menuItem.getName())
                .basePrice(menuItem.getPrice())
                .optionPrice(optionPrice)
                .totalPrice(totalPrice)
                .selectedOptions(selectedOptionInfos)
                .isValid(true)
                .errorMessage(null)
                .build();
    }

    /**
     * 3. 옵션 유효성 검증 (Private)
     * 
     * 검증 항목:
     * 1) 선택한 옵션이 해당 메뉴의 옵션인가?
     * 2) 필수 옵션을 모두 선택했는가?
     * 3) SINGLE 타입에서 2개 이상 선택하지 않았는가?
     */
    private ValidationResult validateOptions(
            MenuItemEntity menuItem, 
            List<OptionItemEntity> selectedOptions) {
        
        log.debug("[유효성 검증] 시작");

        // 검증 1: 선택한 옵션이 이 메뉴의 옵션인지 확인
        Set<Long> menuOptionIds = menuItem.getOptionGroups().stream()
                .flatMap(group -> group.getOptions().stream())
                .map(OptionItemEntity::getOptionItemId)
                .collect(Collectors.toSet());

        for (OptionItemEntity option : selectedOptions) {
            if (!menuOptionIds.contains(option.getOptionItemId())) {
                return ValidationResult.invalid(
                    "유효하지 않은 옵션이 포함되어 있습니다: " + option.getName()
                );
            }
        }

        // 검증 2 & 3: 옵션을 그룹별로 분류
        Map<Long, List<OptionItemEntity>> optionsByGroup = selectedOptions.stream()
                .collect(Collectors.groupingBy(
                    option -> option.getOptionGroup().getOptionGroupId()
                ));

        // 각 옵션 그룹에 대해 검증
        for (OptionGroupEntity group : menuItem.getOptionGroups()) {
            List<OptionItemEntity> selectedInGroup = optionsByGroup.getOrDefault(
                group.getOptionGroupId(), Collections.emptyList()
            );

            // 검증 2: 필수 옵션 체크
            if (group.getIsRequired() && selectedInGroup.isEmpty()) {
                return ValidationResult.invalid(
                    "필수 옵션을 선택해주세요: " + group.getName()
                );
            }

            // 검증 3: SINGLE 타입 체크
            if ("SINGLE".equals(group.getSelectionType()) && selectedInGroup.size() > 1) {
                return ValidationResult.invalid(
                    String.format("단일 선택 옵션입니다. [%s]에서 1개만 선택해주세요.", 
                                group.getName())
                );
            }
        }

        log.debug("[유효성 검증] 통과");
        return ValidationResult.valid();
    }

    /**
     * 유효성 검증 결과 (내부 클래스)
     */
    private static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;

        private ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        public static ValidationResult valid() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult invalid(String errorMessage) {
            return new ValidationResult(false, errorMessage);
        }

        public boolean isValid() {
            return valid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
