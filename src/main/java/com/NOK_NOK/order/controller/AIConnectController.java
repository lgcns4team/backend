package com.NOK_NOK.order.controller;

import com.NOK_NOK.order.domain.dto.AIConnectResponseDto;
import com.NOK_NOK.order.service.AIConnectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AI 연결 컨트롤러
 * 
 * AI 음성인식 시스템과 연결하기 위한 전용 API
 * 메뉴와 옵션 정보를 간소화된 형태로 제공
 * 
 * API:
 * - GET /api/ai/complete-data : 전체 메뉴 + 옵션 데이터 한번에 조회
 */
@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI Connect", description = "AI 음성인식 연결 API")
public class AIConnectController {

    private final AIConnectService aiConnectService;

    /**
     * AI용 전체 데이터 조회 (메뉴 + 옵션 통합)
     * 
     * API: GET /api/ai/complete-data
     * 
     * 응답:
     * {
     *   "menus": [...],
     *   "menuOptions": {...},
     *   "totalMenus": 15,
     *   "totalOptionGroups": 25
     * }
     * 
     * 사용 목적:
     * 1. AI 시스템 초기화 시 한번에 모든 메뉴/옵션 정보 로딩
     * 2. 음성인식용 키워드 매핑 데이터 생성
     * 3. 로컬 캐싱으로 빠른 메뉴 검색
     * 
     * @return 전체 메뉴 및 옵션 정보
     */
    @Operation(
        summary = "AI용 전체 데이터 조회", 
        description = "음성인식 AI 시스템을 위한 전체 메뉴와 옵션 정보를 한번에 조회합니다. " +
                      "간소화된 형태로 제공되어 AI 처리에 최적화되어 있습니다."
    )
    @GetMapping("/complete-data")
    public ResponseEntity<AIConnectResponseDto.CompleteData> getCompleteDataForAI() {
        log.info("[AI API] GET /api/ai/complete-data - AI 전체 데이터 조회 시작");

        AIConnectResponseDto.CompleteData response = aiConnectService.getCompleteDataForAI();

        log.info("[AI API] 전체 데이터 조회 완료 - 메뉴 {}개, 옵션 그룹 {}개", 
                response.getTotalMenus(), response.getTotalOptionGroups());

        return ResponseEntity.ok(response);
    }

    /**
     * AI 연결 상태 확인 (헬스체크용)
     * 
     * API: GET /api/ai/health
     * 
     * @return 서비스 상태
     */
    @Operation(summary = "AI 연결 상태 확인", description = "AI 시스템 연결 상태를 확인합니다.")
    @GetMapping("/health")
    public ResponseEntity<AIConnectResponseDto.HealthCheck> getHealthStatus() {
        log.info("[AI API] GET /api/ai/health - 헬스체크");

        AIConnectResponseDto.HealthCheck health = aiConnectService.getHealthStatus();

        return ResponseEntity.ok(health);
    }
}