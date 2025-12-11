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
     * 시간대 (필수)
     * MORNING: 아침 (06:00~11:59)
     * AFTERNOON: 오후 (12:00~17:59)
     * EVENING: 저녁 (18:00~05:59)
     */
    @Column(name = "time_slot", length = 20)
    private String timeSlot;

    /**
     * 성별 (선택)
     * MALE: 남성
     * FEMALE: 여성
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
     * 세션 생성 시간
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
