package com.NOK_NOK.abs.domain.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 광고 타겟 규칙 Entity
 * 
 * 테이블: ad_target_rule
 */
@Entity
@Table(name = "ad_target_rule")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AdTargetRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Long ruleId;

    /**
     * 광고 컨텐츠
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    private AdContentEntity adContent;

    /**
     * 타겟 나이대
     * 예: "10대", "20대", "30대", "40대", "50대+"
     * NULL: 모든 연령대
     */
    @Column(name = "age_group", length = 20)
    private String ageGroup;

    /**
     * 타겟 성별
     * M: 남성, F: 여성
     * NULL: 모든 성별
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 1)
    private Gender gender;

    /**
     * 성별 enum
     */
    public enum Gender {
        M, F
    }

    /**
     * 타겟 조건과 매칭되는지 확인
     * 
     * @param userAgeGroup 사용자 연령대
     * @param userGender 사용자 성별
     * @return 매칭 여부
     */
    public boolean matches(String userAgeGroup, String userGender) {
        // age_group이 NULL이면 모든 연령대 매칭
        if (this.ageGroup != null && !this.ageGroup.equals(userAgeGroup)) {
            return false;
        }
        
        // gender가 NULL이면 모든 성별 매칭
        if (this.gender != null && userGender != null) {
            if (!this.gender.name().equals(userGender)) {
                return false;
            }
        }
        
        return true;
    }
}