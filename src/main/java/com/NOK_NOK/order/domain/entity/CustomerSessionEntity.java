package com.NOK_NOK.order.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 고객 세션 Entity
 * 
 * 테이블: customer_session
 * 
 * 대상 인식 정보:
 * - timeSlot: 시간대 (자동 계산)
 * - gender: 성별 (선택 - 대상 인식 성공 시)
 * - ageGroup: 연령대 (선택 - 대상 인식 성공 시)
 */
@Entity
@Table(name = "customer_session")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CustomerSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    /**
     * 매장
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    /**
     * 세션 생성 시간
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * 세션 종료 시간
     */
    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    /**
     * 연령대 (선택)
     * 예: "10대", "20대", "30대", "40대", "50대+"
     * NULL: 대상 인식 실패
     */
    @Column(name = "age_group", length = 50)
    private String ageGroup;

    /**
     * 성별 (선택)
     * M: 남성
     * F: 여성
     * NULL: 대상 인식 실패
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 1)
    private Gender gender;

    /**
     * 시니어 모드 여부
     * FALSE: 일반 모드
     * TRUE: 쉬운 주문 모드
     */
    @Column(name = "is_senior_mode", nullable = false)
    @Builder.Default
    private Boolean isSeniorMode = false;

    /**
     * 성별 enum
     */
    public enum Gender {
        M, F
    }

        /**
     * 편의 메서드: 세션 종료
     */
    public void endSession() {
        this.endedAt = LocalDateTime.now();
    }
}
