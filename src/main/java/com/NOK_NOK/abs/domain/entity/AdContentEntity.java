package com.NOK_NOK.abs.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * 광고 컨텐츠 Entity
 * 
 * 테이블: ad_content
 */
@Entity
@Table(name = "ad_content")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AdContentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_id")
    private Long adId;

    /**
     * 광고 이름
     */
    @Column(name = "title", nullable = false, length = 50)
    private String title;

    /**
     * 미디어 타입
     * IMAGE, VIDEO
     */
    @Column(name = "media_type", nullable = false, length = 20)
    private String mediaType;

    /**
     * 미디어 URL (광고 원본 경로)
     */
    @Column(name = "media_url", nullable = false, length = 255)
    private String mediaUrl;

    /**
     * 광고 계약 시작 날짜
     */
    @Column(name = "start_date")
    private LocalDate startDate;

    /**
     * 광고 계약 종료 날짜
     */
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * 사용 여부
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    /**
     * 광고가 활성화 기간인지 확인
     */
    public boolean isInActivePeriod() {
        if (!isActive) {
            return false;
        }
        
        LocalDate now = LocalDate.now();
        
        if (startDate != null && now.isBefore(startDate)) {
            return false;
        }
        
        if (endDate != null && now.isAfter(endDate)) {
            return false;
        }
        
        return true;
    }
}