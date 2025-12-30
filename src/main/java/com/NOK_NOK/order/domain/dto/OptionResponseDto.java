package com.NOK_NOK.order.domain.dto;

import com.NOK_NOK.order.domain.entity.MenuItemEntity;
import com.NOK_NOK.order.domain.entity.OptionGroupEntity;
import com.NOK_NOK.order.domain.entity.OptionItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 옵션 응답 DTO
 */
public class OptionResponseDto {

    /**
     * 메뉴 옵션 조회 응답
     * 
     * API: GET /api/menus/{menuId}/options
     * 
     * 프론트엔드에서 가격 계산:
     * totalPrice = basePrice + sum(선택한 옵션들의 optionPrice)
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuOptionDetail {

        private Long menuId;
        private String menuName;
        private Integer basePrice;
        private List<OptionGroupDto> optionGroups;

        /**
         * Entity → DTO 변환
         */
        public static MenuOptionDetail from(MenuItemEntity menuItem) {
            return MenuOptionDetail.builder()
                    .menuId(menuItem.getMenuId())
                    .menuName(menuItem.getName())
                    .basePrice(menuItem.getPrice())
                    .optionGroups(
                            menuItem.getOptionGroups().stream()
                                    .map(OptionGroupDto::from)
                                    .collect(Collectors.toList()))
                    .build();
        }
    }

    /**
     * 옵션 그룹 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionGroupDto {

        private Long optionGroupId;
        private String name;
        private Boolean isRequired;
        private String selectionType;
        private List<OptionItemDto> options;

        public static OptionGroupDto from(OptionGroupEntity optionGroup) {
            return OptionGroupDto.builder()
                    .optionGroupId(optionGroup.getOptionGroupId())
                    .name(optionGroup.getName())
                    .isRequired(optionGroup.getIsRequired())
                    .selectionType(optionGroup.getSelectionType())
                    .options(
                            optionGroup.getOptions().stream()
                                    .map(OptionItemDto::from)
                                    .collect(Collectors.toList()))
                    .build();
        }
    }

    /**
     * 옵션 아이템 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionItemDto {

        private Long optionItemId;
        private String name;
        private Integer optionPrice;

        public static OptionItemDto from(OptionItemEntity optionItem) {
            return OptionItemDto.builder()
                    .optionItemId(optionItem.getOptionItemId())
                    .name(optionItem.getName())
                    .optionPrice(optionItem.getOptionPrice())
                    .build();
        }
    }
}