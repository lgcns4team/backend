package com.NOK_NOK.order.controller;

import com.NOK_NOK.order.domain.dto.RecommendRequestDto;
import com.NOK_NOK.order.domain.dto.RecommendResponseDto;
import com.NOK_NOK.order.service.RecommendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 추천 메뉴 컨트롤러
 * 
 * API: GET /api/menus/recommend
 * 
 * 3단계 추천 로직:
 * 1. 시간대 + 성별 + 연령대 (대상 인식 성공)
 * 2. 시간대만 (디폴트 - 대상 인식 실패)
 * 3. 전체 인기 메뉴 (최후의 대안)
 */
@Slf4j
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
@Tag(name = "Recommend", description = "추천 메뉴 API")
public class RecommendController {

    private final RecommendService recommendService;

    /**
     * 추천 메뉴 조회
     * 
     * 시간대, 성별, 연령대 기반 추천 메뉴 제공
     * 
     * 대상 인식 성공:
     * GET /api/menus/recommend?timeSlot=MORNING&gender=MALE&ageGroup=20대
     * 
     * 대상 인식 실패 (디폴트):
     * GET /api/menus/recommend?timeSlot=MORNING
     * 
     * @param timeSlot 시간대 (필수)
     * @param gender   성별 (선택)
     * @param ageGroup 연령대 (선택)
     * @param limit    조회 개수 (기본값: 5)
     * @return 추천 메뉴 목록
     */
    @Operation(summary = "추천 메뉴 조회", description = "시간대, 성별, 연령대 기반 추천 메뉴를 제공합니다. " +
            "대상 인식 실패 시 시간대만으로 추천합니다.")
    @GetMapping("/recommend")
    public ResponseEntity<RecommendResponseDto.RecommendList> getRecommendedMenus(
            @Parameter(description = "시간대 (MORNING, AFTERNOON, EVENING)", example = "MORNING", required = true) @RequestParam(name = "timeSlot") String timeSlot,

            @Parameter(description = "성별 (MALE, FEMALE) - 대상 인식 성공 시", example = "MALE") @RequestParam(name = "gender", required = false) String gender,

            @Parameter(description = "연령대 (10대, 20대, 30대, 40대, 50대+) - 대상 인식 성공 시", example = "20대") @RequestParam(name = "ageGroup", required = false) String ageGroup,

            @Parameter(description = "조회 개수", example = "10") @RequestParam(name = "limit", defaultValue = "10") Integer limit) {

        log.info("[API] GET /api/menus/recommend - timeSlot: {}, gender: {}, ageGroup: {}, limit: {}",
                timeSlot, gender, ageGroup, limit);

        // Request DTO 생성
        RecommendRequestDto.Recommend request = RecommendRequestDto.Recommend.builder()
                .timeSlot(timeSlot)
                .gender(gender)
                .ageGroup(ageGroup)
                .limit(limit)
                .build();

        // 추천 메뉴 조회
        RecommendResponseDto.RecommendList response = recommendService.getRecommendedMenus(request);

        return ResponseEntity.ok(response);
    }
}
