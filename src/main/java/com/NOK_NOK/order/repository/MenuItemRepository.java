package com.NOK_NOK.order.repository;

import com.NOK_NOK.order.domain.entity.MenuItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * MenuItem Repository
 */
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Long> {

    // ============================================
    // 기존 메뉴 조회 쿼리
    // ============================================

    /**
     * 카테고리별 메뉴 조회 (페이지네이션)
     */
    Page<MenuItemEntity> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * 카테고리 + 활성화 상태별 메뉴 조회 (페이지네이션)
     */
    Page<MenuItemEntity> findByCategoryIdAndIsActive(Long categoryId, Boolean isActive, Pageable pageable);

    /**
     * 카테고리 + 활성화 상태별 메뉴 조회 (정렬만 적용, 키오스크용)
     */
    List<MenuItemEntity> findByCategoryIdAndIsActive(Long categoryId, Boolean isActive, Sort sort);

    /**
     * 키워드 검색 (메뉴명)
     */
    @Query("SELECT m FROM MenuItemEntity m WHERE " +
            "(:categoryId IS NULL OR m.categoryId = :categoryId) AND " +
            "(:isActive IS NULL OR m.isActive = :isActive) AND " +
            "(:keyword IS NULL OR m.name LIKE %:keyword%)")
    Page<MenuItemEntity> searchMenus(
            @Param("categoryId") Long categoryId,
            @Param("isActive") Boolean isActive,
            @Param("keyword") String keyword,
            Pageable pageable);

    /**
     * 메뉴와 옵션 그룹을 함께 조회
     * 
     * DISTINCT 추가로 MultipleBagFetchException 해결
     * 옵션 아이템은 LAZY 로딩으로 자동 조회됨
     */
    @Query("SELECT DISTINCT m FROM MenuItemEntity m " +
            "LEFT JOIN FETCH m.optionGroups " +
            "WHERE m.menuId = :menuId")
    Optional<MenuItemEntity> findByIdWithOptions(@Param("menuId") Long menuId);

    // ============================================
    // ⭐ 추천 메뉴 쿼리
    // customer_session.created_at으로 time_slot 계산:
    // - HOUR(cs.created_at) BETWEEN 6 AND 11 → 'MORNING'
    // - HOUR(cs.created_at) BETWEEN 12 AND 17 → 'AFTERNOON'
    // - 나머지 → 'EVENING'
    // ============================================

    /**
     * 1순위: 시간대 + 성별 + 연령대 기반 추천
     * 
     * 대상 인식 성공 시 사용
     * customer_session.created_at으로 시간대 판단
     * 
     * @param timeSlot  시간대 ('MORNING', 'AFTERNOON', 'EVENING')
     * @param gender    성별 ('M', 'F')
     * @param ageGroup  연령대 ("10대", "20대", ...)
     * @param startDate 조회 시작 날짜 (최근 3개월)
     * @param limit     조회 개수
     * @return [menuId, orderCount] 배열 리스트
     */
    @Query(value = "SELECT m.menu_id, COUNT(oi.order_item_id) as order_count " +
            "FROM customer_session cs " +
            "JOIN orders o ON cs.session_id = o.session_id " +
            "JOIN order_item oi ON o.order_id = oi.order_id " +
            "JOIN menu_item m ON oi.menu_id = m.menu_id " +
            "WHERE cs.gender = :gender " +
            "  AND cs.age_group = :ageGroup " +
            "  AND m.is_active = 1 " +
            "  AND cs.created_at >= :startDate " +
            "  AND CASE " +
            "        WHEN :timeSlot = 'MORNING' THEN HOUR(cs.created_at) BETWEEN 6 AND 11 " +
            "        WHEN :timeSlot = 'AFTERNOON' THEN HOUR(cs.created_at) BETWEEN 12 AND 17 " +
            "        WHEN :timeSlot = 'EVENING' THEN (HOUR(cs.created_at) >= 18 OR HOUR(cs.created_at) < 6) " +
            "      END " +
            "GROUP BY m.menu_id " +
            "ORDER BY order_count DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Object[]> findRecommendedByFullCondition(
            @Param("timeSlot") String timeSlot,
            @Param("gender") String gender,
            @Param("ageGroup") String ageGroup,
            @Param("startDate") LocalDateTime startDate,
            @Param("limit") int limit);

    /**
     * 2순위: 시간대만 기반 추천 (디폴트)
     * 
     * 대상 인식 실패 시 사용
     * customer_session.created_at으로 시간대 판단
     * 
     * @param timeSlot  시간대
     * @param startDate 조회 시작 날짜
     * @param limit     조회 개수
     * @return [menuId, orderCount] 배열 리스트
     */
    @Query(value = "SELECT m.menu_id, COUNT(oi.order_item_id) as order_count " +
            "FROM customer_session cs " +
            "JOIN orders o ON cs.session_id = o.session_id " +
            "JOIN order_item oi ON o.order_id = oi.order_id " +
            "JOIN menu_item m ON oi.menu_id = m.menu_id " +
            "WHERE m.is_active = 1 " +
            "  AND cs.created_at >= :startDate " +
            "  AND CASE " +
            "        WHEN :timeSlot = 'MORNING' THEN HOUR(cs.created_at) BETWEEN 6 AND 11 " +
            "        WHEN :timeSlot = 'AFTERNOON' THEN HOUR(cs.created_at) BETWEEN 12 AND 17 " +
            "        WHEN :timeSlot = 'EVENING' THEN (HOUR(cs.created_at) >= 18 OR HOUR(cs.created_at) < 6) " +
            "      END " +
            "GROUP BY m.menu_id " +
            "ORDER BY order_count DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Object[]> findRecommendedByTimeSlot(
            @Param("timeSlot") String timeSlot,
            @Param("startDate") LocalDateTime startDate,
            @Param("limit") int limit);

    /**
     * 3순위: 전체 인기 메뉴 (최후의 디폴트)
     * 
     * @param startDate 조회 시작 날짜
     * @param limit     조회 개수
     * @return [menuId, orderCount] 배열 리스트
     */
    @Query(value = "SELECT m.menu_id, COUNT(oi.order_item_id) as order_count " +
            "FROM orders o " +
            "JOIN order_item oi ON o.order_id = oi.order_id " +
            "JOIN menu_item m ON oi.menu_id = m.menu_id " +
            "WHERE m.is_active = 1 " +
            "  AND o.created_at >= :startDate " +
            "GROUP BY m.menu_id " +
            "ORDER BY order_count DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Object[]> findPopularMenus(
            @Param("startDate") LocalDateTime startDate,
            @Param("limit") int limit);

    /**
     * 메뉴 ID 리스트로 메뉴 조회
     * 
     * @param menuIds 메뉴 ID 리스트
     * @return 메뉴 목록
     */
    @Query("SELECT m FROM MenuItemEntity m WHERE m.menuId IN :menuIds")
    List<MenuItemEntity> findByMenuIdIn(@Param("menuIds") List<Long> menuIds);
}