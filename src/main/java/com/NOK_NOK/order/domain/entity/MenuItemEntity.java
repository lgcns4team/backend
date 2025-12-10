package com.NOK_NOK.order.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 메뉴 아이템 Entity
 * 
 * 테이블: menu_item
 * 더미 데이터 기준으로 작성
 */
@Entity
@Table(name = "menu_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MenuItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long menuId;

    /**
     * 카테고리 ID
     * 
     * ⚠️ 현재는 FK 관계 없이 Long만 저장
     * → Category 담당자가 개발 완료하면 @ManyToOne으로 변경
     */
    @Column(name = "category_id")
    private Long categoryId;

    /**
     * 메뉴 이름
     * 예: "아메리카노", "카페라떼"
     */
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * 기본 가격 (옵션 제외)
     * 예: 아메리카노 4000원
     */
    @Column(name = "price", nullable = false)
    private Integer price;

    /**
     * 판매 여부
     * 1: 판매 중, 0: 판매 중지
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    /**
     * 이미지 URL
     */
    @Column(name = "image_url", length = 255)
    private String imageUrl;

    /**
     * 옵션 그룹 목록
     * 
     * 예: 아메리카노 → [사이즈, 얼음량, 샷 추가]
     */
    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OptionGroupEntity> optionGroups = new ArrayList<>();

    /**
     * 편의 메서드: 옵션 그룹 추가
     */
    public void addOptionGroup(OptionGroupEntity optionGroup) {
        this.optionGroups.add(optionGroup);
        optionGroup.setMenuItem(this);
    }
}