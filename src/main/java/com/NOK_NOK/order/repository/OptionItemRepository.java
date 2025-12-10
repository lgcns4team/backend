package com.NOK_NOK.order.repository;

import com.NOK_NOK.order.domain.entity.OptionItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * OptionItem Repository
 * 
 * 현재는 사용하지 않지만, 나중을 위해 유지
 * - 추천 메뉴 기능
 * - 인기 옵션 조회
 * - 옵션 통계
 * - 주문 생성 시 옵션 검증
 */
@Repository
public interface OptionItemRepository extends JpaRepository<OptionItemEntity, Long> {

    /**
     * 여러 옵션을 한 번에 조회 (옵션 그룹 포함)
     * 
     * 사용 예:
     * - 주문 생성 시 선택한 옵션 검증
     * - 추천 메뉴의 인기 옵션 조회
     * 
     * @param optionItemIds 옵션 ID 목록
     * @return 옵션 아이템 목록 (옵션 그룹 포함)
     */
    @Query("SELECT oi FROM OptionItemEntity oi " +
            "JOIN FETCH oi.optionGroup " +
            "WHERE oi.optionItemId IN :optionItemIds")
    List<OptionItemEntity> findByIdInWithGroup(@Param("optionItemIds") List<Long> optionItemIds);

    /**
     * 특정 메뉴의 인기 옵션 조회 (나중에 구현)
     * 
     * 추천 메뉴 기능에서 사용 예정
     * 
     * @param menuId 메뉴 ID
     * @param limit  조회 개수
     * @return 인기 옵션 목록
     */
    // TODO: 나중에 구현
    // List<OptionItemEntity> findPopularOptionsByMenuId(Long menuId, int limit);

    /**
     * 특정 옵션 그룹의 모든 옵션 조회 (나중에 구현)
     * 
     * @param optionGroupId 옵션 그룹 ID
     * @return 옵션 목록
     */
    // TODO: 나중에 구현
    // List<OptionItemEntity> findByOptionGroupId(Long optionGroupId);
}