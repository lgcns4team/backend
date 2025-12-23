package com.NOK_NOK.abs.controller;

import com.NOK_NOK.abs.domain.dto.AdRequestDto;
import com.NOK_NOK.abs.domain.dto.AdResponseDto;
import com.NOK_NOK.abs.service.AdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 광고 컨트롤러
 * 
 * API:
 * 1. GET /api/ads - 전체 활성화 광고 조회
 * 2. GET /api/ads/payment - 결제 후 맞춤형 광고 조회
 * 3. POST /api/ads/display-log - 광고 표시 로그 저장
 */
@Slf4j
@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
@Tag(name = "Advertisement", description = "광고 관리 API")
public class AdController {

    private final AdService adService;

    /**
     * 전체 활성화 광고 조회
     * 
     * API: GET /api/ads
     * 
     * 메인 화면에서 모든 광고를 React로 전달
     * React에서 정해진 로직에 따라 순서대로 표시
     * 각 광고가 끝나면 POST /api/ads/display-log 호출
     * 
     * @return 활성화된 광고 목록
     */
    @Operation(
        summary = "전체 광고 조회",
        description = "메인 화면용 활성화된 모든 광고를 조회합니다. React에서 순서대로 표시합니다."
    )
    @GetMapping
    public ResponseEntity<AdResponseDto.AdList> getAllActiveAds() {
        log.info("[API] GET /api/ads - 전체 광고 조회");

        AdResponseDto.AdList response = adService.getAllActiveAds();

        return ResponseEntity.ok(response);
    }

    /**
     * 결제 후 맞춤형 광고 조회
     * 
     * API: GET /api/ads/payment?ageGroup=20대&gender=M
     * 
     * Frontend에서 저장하고 있던 대상 인식 정보를 파라미터로 전달
     * 해당 조건에 맞는 광고를 타겟팅하여 제공
     * 
     * @param ageGroup 연령대 (선택, Frontend에서 전달)
     * @param gender 성별 (선택, Frontend에서 전달)
     * @return 맞춤형 광고 목록
     */
    @Operation(
        summary = "결제 후 맞춤형 광고 조회",
        description = "Frontend에서 저장하고 있던 대상 인식 정보(연령대, 성별)를 기반으로 타겟팅된 광고를 제공합니다."
    )
    @GetMapping("/payment")
    public ResponseEntity<AdResponseDto.PaymentAdList> getPaymentAds(
            @Parameter(description = "연령대 (예: 20대, 30대)", example = "20대")
            @RequestParam(value = "ageGroup", required = false) String ageGroup,
            
            @Parameter(description = "성별 (M: 남성, F: 여성)", example = "M")
            @RequestParam(value = "gender", required = false) String gender) {

        log.info("[API] GET /api/ads/payment - ageGroup: {}, gender: {}", ageGroup, gender);

        AdResponseDto.PaymentAdList response = adService.getPaymentAds(ageGroup, gender);

        return ResponseEntity.ok(response);
    }

    /**
     * 광고 표시 로그 저장
     * 
     * API: POST /api/ads/display-log
     * 
     * React에서 광고가 끝나면 호출하여 로그 저장
     * 
     * @param request 광고 로그 요청
     * @return 저장 결과
     */
    @Operation(
        summary = "광고 표시 로그 저장",
        description = "광고가 표시된 정보를 로그로 저장합니다. React에서 광고가 끝나면 호출합니다."
    )
    @PostMapping("/display-log")
    public ResponseEntity<AdResponseDto.DisplayLogSaved> saveDisplayLog(
            @RequestBody AdRequestDto.SaveDisplayLog request) {

        log.info("[API] POST /api/ads/display-log - sessionId: {}, adId: {}",
                /*request.getSessionId(),*/ request.getAdId());

        AdResponseDto.DisplayLogSaved response = adService.saveDisplayLog(request);

        return ResponseEntity.ok(response);
    }
}