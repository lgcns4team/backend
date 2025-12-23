package com.NOK_NOK.abs.service;

import com.NOK_NOK.abs.domain.dto.AdRequestDto;
import com.NOK_NOK.abs.domain.dto.AdResponseDto;
import com.NOK_NOK.abs.domain.entity.AdContentEntity;
import com.NOK_NOK.abs.domain.entity.AdDisplayLogEntity;
import com.NOK_NOK.abs.domain.entity.AdTargetRuleEntity;
import com.NOK_NOK.abs.exceptions.AdExceptions.*;
import com.NOK_NOK.abs.repository.AdContentRepository;
import com.NOK_NOK.abs.repository.AdDisplayLogRepository;
import com.NOK_NOK.abs.repository.AdTargetRuleRepository;
import com.NOK_NOK.order.domain.entity.CustomerSessionEntity;
import com.NOK_NOK.order.repository.CustomerSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 광고 서비스
 * 
 * 기능:
 * 1. 전체 광고 조회 (메인 화면용)
 * 2. 결제 후 맞춤형 광고 조회
 * 3. 광고 표시 로그 저장
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdService {

    private final AdContentRepository adContentRepository;
    private final AdTargetRuleRepository adTargetRuleRepository;
    private final AdDisplayLogRepository adDisplayLogRepository;
    private final CustomerSessionRepository customerSessionRepository;

    /**
     * 전체 활성화 광고 조회
     * 
     * API: GET /api/ads
     * 
     * 메인 화면에서 모든 광고를 React로 전달
     * React에서 순서대로 표시 후 각 광고가 끝나면 로그 저장 API 호출
     * 
     * @return 활성화된 광고 목록
     */
    @Transactional(readOnly = true)
    public AdResponseDto.AdList getAllActiveAds() {
        log.info("[광고 조회] 전체 활성화 광고 조회 시작");

        // 현재 날짜 기준 활성화된 광고만 조회
        List<AdContentEntity> activeAds = adContentRepository.findAllActiveAds(LocalDate.now());

        log.info("[광고 조회] 완료 - 광고 개수: {}", activeAds.size());

        return AdResponseDto.AdList.from(activeAds);
    }

    /**
     * 결제 후 맞춤형 광고 조회
     * 
     * API: GET /api/ads/payment?ageGroup=20대&gender=M
     * 
     * Frontend에서 저장하고 있던 대상 인식 정보를 기반으로 타겟팅된 광고 제공
     * 
     * @param ageGroup 연령대 (Frontend에서 전달)
     * @param gender 성별 (Frontend에서 전달)
     * @return 맞춤형 광고 목록
     */
    @Transactional(readOnly = true)
    public AdResponseDto.PaymentAdList getPaymentAds(String ageGroup, String gender) {
        log.info("[맞춤 광고 조회] 시작 - ageGroup: {}, gender: {}", ageGroup, gender);

        // 1. 활성화된 모든 광고 조회
        List<AdContentEntity> activeAds = adContentRepository.findAllActiveAds(LocalDate.now());

        if (activeAds.isEmpty()) {
            log.warn("[맞춤 광고 조회] 활성화된 광고 없음");
            return AdResponseDto.PaymentAdList.builder()
                    .ads(new ArrayList<>())
                    .totalCount(0)
                    .ageGroup(ageGroup)
                    .gender(gender)
                    .message("현재 표시할 광고가 없습니다.")
                    .build();
        }

        // 2. 광고 ID 목록 추출
        List<Long> adIds = activeAds.stream()
                .map(AdContentEntity::getAdId)
                .collect(Collectors.toList());

        // 3. 타겟 규칙 일괄 조회
        List<AdTargetRuleEntity> targetRules = adTargetRuleRepository.findByAdIdIn(adIds);

        // 4. 광고별 타겟 규칙 그룹화
        Map<Long, List<AdTargetRuleEntity>> rulesByAdId = targetRules.stream()
                .collect(Collectors.groupingBy(rule -> rule.getAdContent().getAdId()));

        // 5. 타겟팅 필터링
        List<AdContentEntity> targetedAds = new ArrayList<>();

        for (AdContentEntity ad : activeAds) {
            List<AdTargetRuleEntity> rules = rulesByAdId.get(ad.getAdId());

            // 타겟 규칙이 없으면 모든 사용자에게 표시
            if (rules == null || rules.isEmpty()) {
                targetedAds.add(ad);
                continue;
            }

            // 하나 이상의 규칙과 매칭되면 표시
            boolean matches = rules.stream()
                    .anyMatch(rule -> rule.matches(ageGroup, gender));

            if (matches) {
                targetedAds.add(ad);
            }
        }

        // 6. DTO 변환
        List<AdResponseDto.AdDetail> adDetails = targetedAds.stream()
                .map(AdResponseDto.AdDetail::from)
                .collect(Collectors.toList());

        log.info("[맞춤 광고 조회] 완료 - 전체: {}, 타겟팅: {}", activeAds.size(), targetedAds.size());

        return AdResponseDto.PaymentAdList.builder()
                .ads(adDetails)
                .totalCount(adDetails.size())
                .ageGroup(ageGroup)
                .gender(gender)
                .message(targetedAds.isEmpty() ?
                        "조건에 맞는 광고가 없습니다." :
                        "맞춤 광고를 제공합니다.")
                .build();
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
    @Transactional
    public AdResponseDto.DisplayLogSaved saveDisplayLog(AdRequestDto.SaveDisplayLog request) {
        log.info("[광고 로그 저장] 시작 - sessionId: {}, adId: {}, duration: {}ms",
                request.getSessionId(), request.getAdId(), request.getDurationMs());

        // 1. 유효성 검증
        if (request.getSessionId() == null || request.getAdId() == null) {
            throw new InvalidDisplayLogRequestException("sessionId와 adId는 필수입니다.");
        }

        if (request.getDisplayedAt() == null) {
            throw new InvalidDisplayLogRequestException("displayedAt은 필수입니다.");
        }

        // 2. 세션 조회
        CustomerSessionEntity session = customerSessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new SessionNotFoundException(request.getSessionId()));

        // 3. 광고 조회
        AdContentEntity adContent = adContentRepository.findById(request.getAdId())
                .orElseThrow(() -> new AdNotFoundException(request.getAdId()));

        // 4. 로그 엔티티 생성
        AdDisplayLogEntity displayLog = AdDisplayLogEntity.builder()
                .session(session)
                .adContent(adContent)
                .displayedAt(request.getDisplayedAt())
                .durationMs(request.getDurationMs())
                .build();

        // 5. 로그 저장
        AdDisplayLogEntity savedLog = adDisplayLogRepository.save(displayLog);

        log.info("[광고 로그 저장] 완료 - displayId: {}", savedLog.getDisplayId());

        return AdResponseDto.DisplayLogSaved.builder()
                .displayId(savedLog.getDisplayId())
                .sessionId(savedLog.getSession().getSessionId())
                .adId(savedLog.getAdContent().getAdId())
                .adTitle(savedLog.getAdContent().getTitle())
                .durationMs(savedLog.getDurationMs())
                .message("광고 표시 로그가 저장되었습니다.")
                .build();
    }
}