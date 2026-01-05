package com.NOK_NOK.manager.controller;

import com.NOK_NOK.manager.domain.dto.DashboardResponseDto;
import com.NOK_NOK.manager.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
@Tag(name = "Manager", description = "매니저 관리 API")
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * 대시보드 전체 요약 조회 (기간 선택 지원)
     * GET /api/manager/dashboard/summary
     */
    @Operation(summary = "대시보드 전체 요약 조회", description = "기간별 대시보드 전체 데이터를 조회합니다.")
    @GetMapping("/dashboard/summary")
    public ResponseEntity<DashboardResponseDto.DashboardSummary> getDashboardSummary(
            @Parameter(description = "시작 날짜 (YYYY-MM-DD)", example = "2025-12-31")
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            
            @Parameter(description = "종료 날짜 (YYYY-MM-DD)", example = "2025-12-31") 
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            
            @Parameter(description = "매장 ID (선택사항)", example = "1")
            @RequestParam(name = "storeId", required = false) Long storeId) {
        
        log.info("GET /api/manager/dashboard/summary - startDate: {}, endDate: {}, storeId: {}", 
                startDate, endDate, storeId);

        // 기본값 설정: 둘 다 null이면 오늘 날짜
        if (startDate == null && endDate == null) {
            startDate = LocalDate.now();
            endDate = LocalDate.now();
        } else if (startDate == null) {
            startDate = endDate;
        } else if (endDate == null) {
            endDate = startDate;
        }

        DashboardResponseDto.DashboardSummary response = dashboardService.getDashboardSummary(startDate, endDate, storeId);

        return ResponseEntity.ok(response);
    }

    /**
     * 시간대별 매출 조회 (기간 선택 지원)
     * GET /api/manager/dashboard/hourly-sales
     */
    @Operation(summary = "시간대별 매출 조회", description = "지정된 기간의 시간대별 매출을 조회합니다.")
    @GetMapping("/dashboard/hourly-sales")
    public ResponseEntity<DashboardResponseDto.HourlySales> getHourlySales(
            @Parameter(description = "시작 날짜 (YYYY-MM-DD)", example = "2025-12-31")
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            
            @Parameter(description = "종료 날짜 (YYYY-MM-DD)", example = "2025-12-31")
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            
            @Parameter(description = "매장 ID (선택사항)", example = "1")
            @RequestParam(name = "storeId", required = false) Long storeId) {
        
        log.info("GET /api/manager/dashboard/hourly-sales - startDate: {}, endDate: {}, storeId: {}", 
                startDate, endDate, storeId);

        if (startDate == null && endDate == null) {
            startDate = LocalDate.now();
            endDate = LocalDate.now();
        } else if (startDate == null) {
            startDate = endDate;
        } else if (endDate == null) {
            endDate = startDate;
        }

        DashboardResponseDto.HourlySales response = dashboardService.getHourlySales(startDate, endDate, storeId);

        return ResponseEntity.ok(response);
    }

    /**
     * 일간 매출 조회 (기간 선택 지원)
     * GET /api/manager/dashboard/daily-sales
     */
    @Operation(summary = "일간 매출 조회", description = "지정된 기간의 일간 매출을 조회합니다.")
    @GetMapping("/dashboard/daily-sales")
    public ResponseEntity<DashboardResponseDto.DailySales> getDailySales(
            @Parameter(description = "시작 날짜 (YYYY-MM-DD)", example = "2025-12-25")
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            
            @Parameter(description = "종료 날짜 (YYYY-MM-DD)", example = "2025-12-31")
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            
            @Parameter(description = "매장 ID (선택사항)", example = "1")
            @RequestParam(name = "storeId", required = false) Long storeId) {
        
        log.info("GET /api/manager/dashboard/daily-sales - startDate: {}, endDate: {}, storeId: {}", 
                startDate, endDate, storeId);

        // 기본값: 최근 7일
        if (startDate == null && endDate == null) {
            endDate = LocalDate.now();
            startDate = endDate.minusDays(6); // 7일간 (오늘 포함)
        } else if (startDate == null) {
            startDate = endDate.minusDays(6);
        } else if (endDate == null) {
            endDate = LocalDate.now();
        }

        DashboardResponseDto.DailySales response = dashboardService.getDailySales(startDate, endDate, storeId);

        return ResponseEntity.ok(response);
    }

    /**
     * 인기메뉴 TOP5 조회 (기간 선택 지원)
     * GET /api/manager/dashboard/popular-menus
     */
    @Operation(summary = "인기메뉴 TOP5 조회", description = "지정된 기간의 인기메뉴 TOP5를 조회합니다.")
    @GetMapping("/dashboard/popular-menus")
    public ResponseEntity<DashboardResponseDto.PopularMenus> getPopularMenus(
            @Parameter(description = "시작 날짜 (YYYY-MM-DD)", example = "2025-12-31")
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            
            @Parameter(description = "종료 날짜 (YYYY-MM-DD)", example = "2025-12-31")
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            
            @Parameter(description = "매장 ID (선택사항)", example = "1")
            @RequestParam(name = "storeId", required = false) Long storeId,
            
            @Parameter(description = "조회할 메뉴 개수", example = "5")
            @RequestParam(name = "limit", defaultValue = "5") int limit) {
        
        log.info("GET /api/manager/dashboard/popular-menus - startDate: {}, endDate: {}, storeId: {}, limit: {}", 
                startDate, endDate, storeId, limit);

        if (startDate == null && endDate == null) {
            startDate = LocalDate.now();
            endDate = LocalDate.now();
        } else if (startDate == null) {
            startDate = endDate;
        } else if (endDate == null) {
            endDate = startDate;
        }

        DashboardResponseDto.PopularMenus response = dashboardService.getPopularMenus(startDate, endDate, storeId, limit);

        return ResponseEntity.ok(response);
    }

    /**
     * 고객 성별/연령대 분석 (기간 선택 지원)
     * GET /api/manager/dashboard/customer-demographics
     */
    @Operation(summary = "고객 성별/연령대 분석", description = "지정된 기간의 고객 성별과 연령대를 분석합니다.")
    @GetMapping("/dashboard/customer-demographics")
    public ResponseEntity<DashboardResponseDto.CustomerDemographics> getCustomerDemographics(
            @Parameter(description = "시작 날짜 (YYYY-MM-DD)", example = "2025-12-31")
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            
            @Parameter(description = "종료 날짜 (YYYY-MM-DD)", example = "2025-12-31")
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            
            @Parameter(description = "매장 ID (선택사항)", example = "1")
            @RequestParam(name = "storeId", required = false) Long storeId) {
        
        log.info("GET /api/manager/dashboard/customer-demographics - startDate: {}, endDate: {}, storeId: {}", 
                startDate, endDate, storeId);

        if (startDate == null && endDate == null) {
            startDate = LocalDate.now();
            endDate = LocalDate.now();
        } else if (startDate == null) {
            startDate = endDate;
        } else if (endDate == null) {
            endDate = startDate;
        }

        DashboardResponseDto.CustomerDemographics response = dashboardService.getCustomerDemographics(startDate, endDate, storeId);

        return ResponseEntity.ok(response);
    }
}