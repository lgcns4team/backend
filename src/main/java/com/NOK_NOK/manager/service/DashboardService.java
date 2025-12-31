package com.NOK_NOK.manager.service;

import com.NOK_NOK.manager.domain.dto.DashboardResponseDto;
import com.NOK_NOK.manager.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    /**
     * 대시보드 전체 요약 조회 (메인 API)
     */
    public DashboardResponseDto.DashboardSummary getDashboardSummary(LocalDate startDate, LocalDate endDate, Long storeId) {
        log.info("Getting dashboard summary for period: {} ~ {}, store: {}", startDate, endDate, storeId);

        // 1. 핵심 지표 조회
        Long totalSales = dashboardRepository.getTotalSales(startDate, endDate, storeId);
        Integer totalOrders = dashboardRepository.getTotalOrders(startDate, endDate, storeId);
        Integer totalCustomers = dashboardRepository.getTotalCustomers(startDate, endDate, storeId);
        Double avgOrderAmount = (totalOrders > 0) ? (double) totalSales / totalOrders : 0.0;

        // 2. 성별 비율 조회
        DashboardResponseDto.GenderRatio genderRatio = getGenderRatio(startDate, endDate, storeId);

        // 3. 연령대 비율 조회
        List<DashboardResponseDto.AgeGroupRatio> ageGroupRatios = getAgeGroupRatios(startDate, endDate, storeId);

        // 4. 시간대별 매출 (10시~16시)
        List<DashboardResponseDto.HourlyData> hourlySales = getHourlyDataList(startDate, endDate, storeId);

        // 5. 일간 매출 (최근 7일)
        List<DashboardResponseDto.DailyData> dailySales = getDailyDataList(startDate, endDate, storeId);

        // 6. 인기메뉴 TOP5
        List<DashboardResponseDto.PopularMenu> popularMenus = getPopularMenusList(startDate, endDate, storeId, 5);

        return DashboardResponseDto.DashboardSummary.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalSales(totalSales != null ? totalSales : 0L)
                .avgOrderAmount(Math.round(avgOrderAmount * 100.0) / 100.0)
                .totalOrders(totalOrders != null ? totalOrders : 0)
                .totalCustomers(totalCustomers != null ? totalCustomers : 0)
                .genderRatio(genderRatio)
                .ageGroupRatios(ageGroupRatios)
                .hourlySales(hourlySales)
                .dailySales(dailySales)
                .popularMenus(popularMenus)
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    /**
     * 시간대별 매출 조회
     */
    public DashboardResponseDto.HourlySales getHourlySales(LocalDate startDate, LocalDate endDate, Long storeId) {
        log.info("Getting hourly sales for period: {} ~ {}, store: {}", startDate, endDate, storeId);

        List<DashboardResponseDto.HourlyData> hourlyData = getHourlyDataList(startDate, endDate, storeId);
        
        Long totalAmount = hourlyData.stream().mapToLong(DashboardResponseDto.HourlyData::getAmount).sum();
        Integer totalOrders = hourlyData.stream().mapToInt(DashboardResponseDto.HourlyData::getOrderCount).sum();

        return DashboardResponseDto.HourlySales.builder()
                .startDate(startDate)
                .endDate(endDate)
                .hourlyData(hourlyData)
                .totalAmount(totalAmount)
                .totalOrders(totalOrders)
                .build();
    }

    /**
     * 일간 매출 조회
     */
    public DashboardResponseDto.DailySales getDailySales(LocalDate startDate, LocalDate endDate, Long storeId) {
        log.info("Getting daily sales for period: {} ~ {}, store: {}", startDate, endDate, storeId);

        List<DashboardResponseDto.DailyData> dailyData = getDailyDataList(startDate, endDate, storeId);
        
        Long totalAmount = dailyData.stream().mapToLong(DashboardResponseDto.DailyData::getAmount).sum();
        Integer totalOrders = dailyData.stream().mapToInt(DashboardResponseDto.DailyData::getOrderCount).sum();
        Integer totalDays = (int) startDate.datesUntil(endDate.plusDays(1)).count();

        return DashboardResponseDto.DailySales.builder()
                .startDate(startDate)
                .endDate(endDate)
                .dailyData(dailyData)
                .totalAmount(totalAmount)
                .totalOrders(totalOrders)
                .totalDays(totalDays)
                .build();
    }

    /**
     * 인기메뉴 조회
     */
    public DashboardResponseDto.PopularMenus getPopularMenus(LocalDate startDate, LocalDate endDate, Long storeId, int limit) {
        log.info("Getting popular menus for period: {} ~ {}, store: {}, limit: {}", startDate, endDate, storeId, limit);

        List<DashboardResponseDto.PopularMenu> menus = getPopularMenusList(startDate, endDate, storeId, limit);

        return DashboardResponseDto.PopularMenus.builder()
                .startDate(startDate)
                .endDate(endDate)
                .menus(menus)
                .totalMenuCount(menus.size())
                .build();
    }

    /**
     * 고객 성별/연령대 분석
     */
    public DashboardResponseDto.CustomerDemographics getCustomerDemographics(LocalDate startDate, LocalDate endDate, Long storeId) {
        log.info("Getting customer demographics for period: {} ~ {}, store: {}", startDate, endDate, storeId);

        DashboardResponseDto.GenderRatio genderRatio = getGenderRatio(startDate, endDate, storeId);
        List<DashboardResponseDto.AgeGroupRatio> ageGroupRatios = getAgeGroupRatios(startDate, endDate, storeId);
        
        Integer totalCustomers = dashboardRepository.getTotalCustomers(startDate, endDate, storeId);
        Integer seniorModeUsers = dashboardRepository.getSeniorModeUsers(startDate, endDate, storeId);

        return DashboardResponseDto.CustomerDemographics.builder()
                .startDate(startDate)
                .endDate(endDate)
                .genderRatio(genderRatio)
                .ageGroupRatios(ageGroupRatios)
                .totalCustomers(totalCustomers != null ? totalCustomers : 0)
                .seniorModeUsers(seniorModeUsers != null ? seniorModeUsers : 0)
                .build();
    }

    // ===== Private Helper Methods =====

    /**
     * 성별 비율 계산
     */
    private DashboardResponseDto.GenderRatio getGenderRatio(LocalDate startDate, LocalDate endDate, Long storeId) {
        Map<String, Object> genderStats = dashboardRepository.getGenderStats(startDate, endDate, storeId);
        
        Integer maleCount = ((Number) genderStats.getOrDefault("maleCount", 0)).intValue();
        Integer femaleCount = ((Number) genderStats.getOrDefault("femaleCount", 0)).intValue();
        Integer totalCount = maleCount + femaleCount;
        
        double malePercentage = totalCount > 0 ? Math.round((double) maleCount / totalCount * 100.0 * 100.0) / 100.0 : 0.0;
        double femalePercentage = totalCount > 0 ? Math.round((double) femaleCount / totalCount * 100.0 * 100.0) / 100.0 : 0.0;
        
        return DashboardResponseDto.GenderRatio.builder()
                .maleCount(maleCount)
                .femaleCount(femaleCount)
                .malePercentage(malePercentage)
                .femalePercentage(femalePercentage)
                .totalCount(totalCount)
                .build();
    }

    /**
     * 연령대별 비율 계산
     */
    private List<DashboardResponseDto.AgeGroupRatio> getAgeGroupRatios(LocalDate startDate, LocalDate endDate, Long storeId) {
        List<Map<String, Object>> ageGroupStats = dashboardRepository.getAgeGroupStats(startDate, endDate, storeId);
        
        int totalCustomers = ageGroupStats.stream()
                .mapToInt(stat -> ((Number) stat.get("customerCount")).intValue())
                .sum();

        // 연령대 순서 정의
        List<String> ageGroupOrder = Arrays.asList("10대", "20대", "30대", "40대", "50대", "60대이상");
        Map<String, String> ageGroupColors = Map.of(
                "10대", "#FF6B6B",
                "20대", "#4ECDC4", 
                "30대", "#45B7D1",
                "40대", "#96CEB4",
                "50대", "#FFEAA7",
                "60대이상", "#DDA0DD"
        );

        return ageGroupOrder.stream()
                .map(ageGroup -> {
                    Optional<Map<String, Object>> stat = ageGroupStats.stream()
                            .filter(s -> ageGroup.equals(s.get("ageGroup")))
                            .findFirst();
                    
                    int count = stat.map(s -> ((Number) s.get("customerCount")).intValue()).orElse(0);
                    double percentage = totalCustomers > 0 ? Math.round((double) count / totalCustomers * 100.0 * 100.0) / 100.0 : 0.0;
                    
                    return DashboardResponseDto.AgeGroupRatio.builder()
                            .ageGroup(ageGroup)
                            .count(count)
                            .percentage(percentage)
                            .color(ageGroupColors.get(ageGroup))
                            .build();
                })
                .filter(ratio -> ratio.getCount() > 0) // 데이터가 있는 연령대만 포함
                .collect(Collectors.toList());
    }

    /**
     * 시간대별 데이터 리스트 생성 (10시~16시)
     */
    private List<DashboardResponseDto.HourlyData> getHourlyDataList(LocalDate startDate, LocalDate endDate, Long storeId) {
        List<Map<String, Object>> hourlySalesData = dashboardRepository.getHourlySales(startDate, endDate, storeId);
        
        // 시간대별 데이터를 Map으로 변환
        Map<Integer, Map<String, Object>> hourlyMap = hourlySalesData.stream()
                .collect(Collectors.toMap(
                        data -> ((Number) data.get("hour")).intValue(),
                        data -> data
                ));

        // 10시~16시 범위로 데이터 생성
        return IntStream.rangeClosed(10, 16)
                .mapToObj(hour -> {
                    Map<String, Object> data = hourlyMap.getOrDefault(hour, Map.of("amount", 0L, "orderCount", 0));
                    
                    return DashboardResponseDto.HourlyData.builder()
                            .hour(hour)
                            .timeLabel(hour + "시")
                            .amount(((Number) data.getOrDefault("amount", 0)).longValue())
                            .orderCount(((Number) data.getOrDefault("orderCount", 0)).intValue())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 일간 데이터 리스트 생성
     */
    private List<DashboardResponseDto.DailyData> getDailyDataList(LocalDate startDate, LocalDate endDate, Long storeId) {
        List<Map<String, Object>> dailySalesData = dashboardRepository.getDailySales(startDate, endDate, storeId);
        
        // 일별 데이터를 Map으로 변환
        Map<LocalDate, Map<String, Object>> dailyMap = dailySalesData.stream()
                .collect(Collectors.toMap(
                        data -> ((java.sql.Date) data.get("saleDate")).toLocalDate(),
                        data -> data
                ));

        LocalDate today = LocalDate.now();
        
        // 기간 내 모든 날짜에 대해 데이터 생성
        return startDate.datesUntil(endDate.plusDays(1))
                .map(date -> {
                    Map<String, Object> data = dailyMap.getOrDefault(date, Map.of("amount", 0L, "orderCount", 0));
                    
                    return DashboardResponseDto.DailyData.builder()
                            .date(date)
                            .dateLabel(date.format(DateTimeFormatter.ofPattern("M/d")))
                            .dayOfWeek(getDayOfWeekKorean(date.getDayOfWeek()))
                            .amount(((Number) data.getOrDefault("amount", 0)).longValue())
                            .orderCount(((Number) data.getOrDefault("orderCount", 0)).intValue())
                            .isToday(date.equals(today))
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 인기메뉴 리스트 생성
     */
    private List<DashboardResponseDto.PopularMenu> getPopularMenusList(LocalDate startDate, LocalDate endDate, Long storeId, int limit) {
        List<Map<String, Object>> popularMenusData = dashboardRepository.getPopularMenus(startDate, endDate, storeId, limit);
        
        return IntStream.range(0, popularMenusData.size())
                .mapToObj(index -> {
                    Map<String, Object> data = popularMenusData.get(index);
                    
                    return DashboardResponseDto.PopularMenu.builder()
                            .menuId(((Number) data.get("menuId")).longValue())
                            .menuName((String) data.get("menuName"))
                            .categoryName((String) data.get("categoryName"))
                            .orderCount(((Number) data.get("orderCount")).intValue())
                            .totalQuantity(((Number) data.get("totalQuantity")).intValue())
                            .totalSales(((Number) data.get("totalSales")).longValue())
                            .imageUrl((String) data.get("imageUrl"))
                            .rank(index + 1) // 1부터 시작하는 순위
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 요일을 한국어로 변환
     */
    private String getDayOfWeekKorean(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> "월";
            case TUESDAY -> "화";
            case WEDNESDAY -> "수";
            case THURSDAY -> "목";
            case FRIDAY -> "금";
            case SATURDAY -> "토";
            case SUNDAY -> "일";
        };
    }
}