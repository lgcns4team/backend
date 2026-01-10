package com.NOK_NOK.order.domain.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 옵션 아이템 Entity
 * 
 * 테이블: option_item
 * 예: "Regular", "Large", "샷 1개 추가"
 */
@Entity
@Table(name = "option_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OptionItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_item_id")
    private Long optionItemId;

    /**
     * 옵션 이름
     * 예: "Regular", "Large", "얼음 적게"
     */
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * 옵션 추가 가격
     * 
     * 더미 데이터 예:
     * - Regular: 0원
     * - Large: 500원
     * - 샷 1개 추가: 500원
     * - 샷 2개 추가: 1000원
     */
    @Column(name = "option_price", nullable = false)
    private Integer optionPrice;

    /**
     * 옵션 그룹
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_group_id", nullable = false)
    @Setter
    private OptionGroupEntity optionGroup;

    /**
     * 추가 비용이 있는지 확인
     */
    public boolean hasExtraPrice() {
        return this.optionPrice != null && this.optionPrice > 0;
    }
}