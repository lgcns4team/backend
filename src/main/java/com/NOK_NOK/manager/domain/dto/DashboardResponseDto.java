package com.NOK_NOK.manager.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DashboardResponseDto {

    /**
     * 대시보드 전체 요약 (UI의 메인 대시보드용)
     * 모든 정보를 한 번의 API 호출로 제공
     */
    @Getter
    @Builder
    public static class DashboardSummary {
        // 기간 정보
        private LocalDate startDate;
        private LocalDate endDate;
        
        // 핵심 지표
        private Long totalSales;          // 총매출 (₩495,194)
        private Double avgOrderAmount;    // 평균 주문 금액 (₩5,692)
        private Integer totalOrders;      // 총 주문 건수
        private Integer totalCustomers;   // 총 고객 수
        
        // 성별/연령대 비율
        private GenderRatio genderRatio;
        private List<AgeGroupRatio> ageGroupRatios;
        
        // 차트 데이터
        private List<HourlyData> hourlySales;      // 시간대별 매출
        private List<DailyData> dailySales;        // 일간 매출
        private List<PopularMenu> popularMenus;   // 인기메뉴 TOP5
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime lastUpdated;
    }

    /**
     * 성별 비율 (도넛 차트용)
     */
    @Getter
    @Builder
    public static class GenderRatio {
        private Integer maleCount;
        private Integer femaleCount;
        private Double malePercentage;
        private Double femalePercentage;
        private Integer totalCount;
    }

    /**
     * 연령대별 비율 (도넛 차트용)
     */
    @Getter
    @Builder
    public static class AgeGroupRatio {
        private String ageGroup;      // "10대", "20대", "30대", "40대", "50대", "60대이상"
        private Integer count;
        private Double percentage;
        private String color;         // 차트 색상 (선택사항)
    }

    /**
     * 시간대별 데이터 (라인 차트용)
     */
    @Getter
    @Builder
    public static class HourlyData {
        private Integer hour;         // 10, 11, 12, 13, 14, 15, 16
        private String timeLabel;     // "10시", "11시"
        private Long amount;          // 해당 시간대 매출
        private Integer orderCount;   // 해당 시간대 주문 건수
    }

    /**
     * 일간 데이터 (바 차트용)
     */
    @Getter
    @Builder
    public static class DailyData {
        private LocalDate date;       // 2025-12-25, 12-26, ...
        private String dateLabel;     // "12/25", "12/26"
        private String dayOfWeek;     // "월", "화", "수"
        private Long amount;          // 해당 날짜 매출
        private Integer orderCount;   // 해당 날짜 주문 건수
        private boolean isToday;      // 오늘 여부 (하이라이트용)
    }

    /**
     * 인기메뉴 (TOP5 리스트용)
     */
    @Getter
    @Builder
    public static class PopularMenu {
        private Long menuId;
        private String menuName;      // "라떼", "아메리카노", "디카페인", "카푸치노", "바닐라라떼"
        private String categoryName;
        private Integer orderCount;   // 주문 횟수
        private Integer totalQuantity; // 총 판매 수량  
        private Long totalSales;      // 총 매출액
        private String imageUrl;
        private Integer rank;         // 순위 (1, 2, 3, 4, 5)
    }

    /**
     * 시간대별 매출 (별도 API 응답용)
     */
    @Getter
    @Builder
    public static class HourlySales {
        private LocalDate startDate;
        private LocalDate endDate;
        private List<HourlyData> hourlyData;
        private Long totalAmount;     // 전체 시간대 총 매출
        private Integer totalOrders;  // 전체 시간대 총 주문 건수
    }

    /**
     * 일간 매출 (별도 API 응답용)
     */
    @Getter
    @Builder
    public static class DailySales {
        private LocalDate startDate;
        private LocalDate endDate;
        private List<DailyData> dailyData;
        private Long totalAmount;     // 전체 기간 총 매출
        private Integer totalOrders;  // 전체 기간 총 주문 건수
        private Integer totalDays;    // 조회한 총 일수
    }

    /**
     * 인기메뉴 (별도 API 응답용)
     */
    @Getter
    @Builder
    public static class PopularMenus {
        private LocalDate startDate;
        private LocalDate endDate;
        private List<PopularMenu> menus;
        private Integer totalMenuCount; // 전체 메뉴 수
    }

    /**
     * 고객 성별/연령대 분석 (별도 API 응답용)
     */
    @Getter
    @Builder
    public static class CustomerDemographics {
        private LocalDate startDate;
        private LocalDate endDate;
        private GenderRatio genderRatio;
        private List<AgeGroupRatio> ageGroupRatios;
        private Integer totalCustomers;
        private Integer seniorModeUsers;   // 시니어 모드 사용자 수
        
        // 시간대별 고객 분석 (선택사항)
        private List<TimeSlotDemographic> timeSlotDemographics;
    }

    /**
     * 시간대별 고객 분석
     */
    @Getter
    @Builder
    public static class TimeSlotDemographic {
        private String timeSlot;      // "10-11", "11-12"
        private Integer totalCustomers;
        private GenderRatio genderRatio;
        private List<AgeGroupRatio> ageGroupRatios;
    }
}