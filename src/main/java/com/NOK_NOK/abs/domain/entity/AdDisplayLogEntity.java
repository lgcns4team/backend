package com.NOK_NOK.abs.domain.entity;

// import com.NOK_NOK.order.domain.entity.CustomerSessionEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 광고 표시 로그 Entity
 * 
 * 테이블: ad_display_log
 */
@Entity
@Table(name = "ad_display_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AdDisplayLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "display_id")
    private Long displayId;

    // /**
    //  * 고객 세션
    //  */
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "session_id", nullable = false)
    // private CustomerSessionEntity session;

    /**
     * 광고 컨텐츠
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    private AdContentEntity adContent;

    /**
     * 표시 시작 시간
     */
    @Column(name = "displayed_at", nullable = false)
    private LocalDateTime displayedAt;

    /**
     * 노출 시간 (밀리초)
     */
    @Column(name = "duration_ms")
    private Integer durationMs;
}