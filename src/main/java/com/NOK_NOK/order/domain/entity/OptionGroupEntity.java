package com.NOK_NOK.order.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 옵션 그룹 Entity
 * 
 * 테이블: option_group
 * 예: "사이즈", "얼음량", "샷 추가"
 */
@Entity
@Table(name = "option_group")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OptionGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_group_id")
    private Long optionGroupId;

    /**
     * 그룹 이름
     * 예: "사이즈", "얼음량"
     */
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * 필수 선택 여부
     * 1: 필수, 0: 선택
     * 
     * 더미 데이터:
     * - 사이즈: 필수 (1)
     * - 얼음량: 선택 (0)
     */
    @Column(name = "is_required", nullable = false)
    private Boolean isRequired;

    /**
     * 선택 타입
     * "SINGLE": 단일 선택 (하나만)
     * "MULTI": 다중 선택 (여러 개)
     * 
     * 더미 데이터:
     * - 사이즈: SINGLE
     * - 샷 추가: MULTI
     */
    @Column(name = "selection_type", nullable = false, length = 20)
    private String selectionType;

    /**
     * 메뉴 아이템
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @Setter
    private MenuItemEntity menuItem;

    /**
     * 옵션 아이템 목록
     * 
     * 예: 사이즈 그룹 → [Regular, Large]
     */
    @OneToMany(mappedBy = "optionGroup", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OptionItemEntity> options = new ArrayList<>();

    /**
     * 편의 메서드: 옵션 추가
     */
    public void addOption(OptionItemEntity optionItem) {
        this.options.add(optionItem);
        optionItem.setOptionGroup(this);
    }
}
