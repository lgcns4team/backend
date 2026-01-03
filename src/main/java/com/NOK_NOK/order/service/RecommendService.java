package com.NOK_NOK.order.service;

import com.NOK_NOK.order.domain.dto.RecommendRequestDto;
import com.NOK_NOK.order.domain.dto.RecommendResponseDto;
import com.NOK_NOK.order.domain.entity.CategoryEntity;
import com.NOK_NOK.order.domain.entity.MenuItemEntity;
import com.NOK_NOK.order.repository.CategoryRepository;
import com.NOK_NOK.order.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 추천 메뉴 서비스
 * 
 * 3단계 추천 로직:
 * 1순위: 시간대 + 성별 + 연령대 (대상 인식 성공)
 * 2순위: 시간대만 (디폴트 - 대상 인식 실패)
 * 3순위: 전체 인기 메뉴 (최후의 대안)
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 추천 메뉴 조회
     * 
     * @param request 추천 조건
     * @return 추천 메뉴 목록
     */
    public RecommendResponseDto.RecommendList getRecommendedMenus(RecommendRequestDto.Recommend request) {
        log.info("[추천 메뉴 조회] 시작 - timeSlot: {}, gender: {}, ageGroup: {}",
                request.getTimeSlot(), request.getGender(), request.getAgeGroup());

        // 최근 3개월 데이터 기준
        LocalDateTime startDate = LocalDateTime.now().minusMonths(3);

        // 3단계 추천 로직
        RecommendResult result = getRecommendedMenusWithFallback(
                request.getTimeSlot(),
                request.getGender(),
                request.getAgeGroup(),
                startDate,
                request.getLimit());

        // 카테고리 정보 조회 (N+1 방지)
        Map<Long, String> categoryNameMap = getCategoryNameMap();

        // DTO 변환
        List<RecommendResponseDto.RecommendedMenu> recommendedMenus = convertToRecommendedMenus(result, categoryNameMap,
                request);

        log.info("[추천 메뉴 조회] 완료 - 추천 타입: {}, 메뉴 개수: {}",
                result.type, recommendedMenus.size());

        return RecommendResponseDto.RecommendList.builder()
                .timeSlot(request.getTimeSlot())
                .gender(request.getGender())
                .ageGroup(request.getAgeGroup())
                .recommendedMenus(recommendedMenus)
                .totalCount(recommendedMenus.size())
                .recommendType(result.type)
                .build();
    }

    /**
     * 3단계 폴백 추천 로직
     */
    private RecommendResult getRecommendedMenusWithFallback(
            String timeSlot, String gender, String ageGroup,
            LocalDateTime startDate, int limit) {

        // 1순위: 시간대 + 성별 + 연령대
        if (gender != null && ageGroup != null) {
            log.info("[1순위] 시간대 + 성별 + 연령대 기반 조회 시도");
            List<Object[]> result = menuItemRepository.findRecommendedByFullCondition(
                    timeSlot, gender, ageGroup, startDate, limit);

            if (!result.isEmpty()) {
                log.info("[1순위] 성공 - {} 개 메뉴 발견", result.size());
                return new RecommendResult(result, "FULL");
            }
            log.info("[1순위] 실패 - 데이터 없음, 2순위로 이동");
        }

        // 2순위: 시간대만 (디폴트)
        log.info("[2순위] 시간대만 기반 조회 시도");
        List<Object[]> result = menuItemRepository.findRecommendedByTimeSlot(
                timeSlot, startDate, limit);

        if (!result.isEmpty()) {
            log.info("[2순위] 성공 - {} 개 메뉴 발견", result.size());
            return new RecommendResult(result, "TIME_ONLY");
        }
        log.info("[2순위] 실패 - 데이터 없음, 3순위로 이동");

        // 3순위: 전체 인기 메뉴
        log.info("[3순위] 전체 인기 메뉴 조회");
        result = menuItemRepository.findPopularMenus(startDate, limit);
        log.info("[3순위] 완료 - {} 개 메뉴 발견", result.size());

        return new RecommendResult(result, "POPULAR");
    }

    /**
     * 카테고리 ID -> 이름 매핑 생성
     */
    private Map<Long, String> getCategoryNameMap() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        Map<Long, String> categoryNameMap = new HashMap<>();

        for (CategoryEntity category : categories) {
            categoryNameMap.put(category.getCategoryId(), category.getName());
        }

        return categoryNameMap;
    }

    /**
     * RecommendResult -> RecommendedMenu DTO 변환
     */
    private List<RecommendResponseDto.RecommendedMenu> convertToRecommendedMenus(
            RecommendResult result,
            Map<Long, String> categoryNameMap,
            RecommendRequestDto.Recommend request) {

        // Object[] -> Map 변환 (menuId -> orderCount)
        Map<Long, Long> orderCountMap = new LinkedHashMap<>();
        for (Object[] row : result.data) {
            Long menuId = ((Number) row[0]).longValue();
            Long orderCount = ((Number) row[1]).longValue();
            orderCountMap.put(menuId, orderCount);
        }

        // 메뉴 ID 리스트
        List<Long> menuIds = new ArrayList<>(orderCountMap.keySet());

        if (menuIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 메뉴 정보 조회
        List<MenuItemEntity> menus = menuItemRepository.findByMenuIdIn(menuIds);

        // menuId -> MenuItemEntity 매핑
        Map<Long, MenuItemEntity> menuMap = menus.stream()
                .collect(Collectors.toMap(MenuItemEntity::getMenuId, menu -> menu));

        // DTO 변환
        List<RecommendResponseDto.RecommendedMenu> recommendedMenus = new ArrayList<>();
        int rank = 1;

        for (Long menuId : menuIds) {
            MenuItemEntity menu = menuMap.get(menuId);
            if (menu == null)
                continue;

            Long orderCount = orderCountMap.get(menuId);

            recommendedMenus.add(
                    RecommendResponseDto.RecommendedMenu.builder()
                            .menuId(menu.getMenuId())
                            .menuName(menu.getName())
                            .categoryName(categoryNameMap.get(menu.getCategoryId()))
                            .basePrice(menu.getPrice())
                            .imageUrl(menu.getImageUrl())
                            .orderCount(orderCount)
                            .popularRank(rank++)
                            .recommendReason(buildRecommendReason(result.type, request, rank - 1))
                            .build());
        }

        return recommendedMenus;
    }

    /**
     * 추천 이유 생성
     */
    private String buildRecommendReason(String type, RecommendRequestDto.Recommend request, int rank) {
        String timeText = getTimeSlotText(request.getTimeSlot());

        switch (type) {
            case "FULL":
                String genderText = "MALE".equals(request.getGender()) ? "남성" : "여성";
                return String.format("%s %s %s에게 인기 %d위",
                        timeText, request.getAgeGroup(), genderText, rank);
            case "TIME_ONLY":
                return String.format("%s 시간대 인기 %d위", timeText, rank);
            case "POPULAR":
                return String.format("전체 인기 %d위", rank);
            default:
                return String.format("추천 메뉴 %d위", rank);
        }
    }

    /**
     * 시간대 텍스트 변환
     */
    private String getTimeSlotText(String timeSlot) {
        switch (timeSlot) {
            case "MORNING":
                return "아침";
            case "AFTERNOON":
                return "오후";
            case "EVENING":
                return "저녁";
            default:
                return "";
        }
    }

    /**
     * 추천 결과 내부 클래스
     */
    private static class RecommendResult {
        List<Object[]> data; // [menuId, orderCount]
        String type; // FULL, TIME_ONLY, POPULAR

        RecommendResult(List<Object[]> data, String type) {
            this.data = data;
            this.type = type;
        }
    }
}