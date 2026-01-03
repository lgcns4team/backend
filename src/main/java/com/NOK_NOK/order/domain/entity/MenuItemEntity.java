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

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;

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
