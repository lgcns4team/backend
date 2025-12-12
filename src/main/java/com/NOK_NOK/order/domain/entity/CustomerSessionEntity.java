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
 * - timeSlot: 시간대 (필수)
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
     * 성별 (선택)
     * DB: ENUM('M','F')
     * NULL: 대상 인식 실패
     */
    @Column(name = "gender", length = 10)
    private String gender;

    /**
     * 연령대 (선택)
     * 예: "10대", "20대", "30대", "40대", "50대+"
     * NULL: 대상 인식 실패
     */
    @Column(name = "age_group", length = 10)
    private String ageGroup;

    /**
     * 세션 생성 시간 (주문 시작 시간)
     * 
     * ⭐ 이 시간으로 time_slot 계산!
     * - 06:00~11:59 → MORNING
     * - 12:00~17:59 → AFTERNOON
     * - 18:00~05:59 → EVENING
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * 세션 종료 시간 (주문 완료 시간)
     */
    @Column(name = "ended_at")
    private LocalDateTime endedAt;
}
