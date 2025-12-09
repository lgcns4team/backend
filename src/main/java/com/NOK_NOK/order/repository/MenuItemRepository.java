package com.NOK_NOK.order.repository;

import com.NOK_NOK.order.domain.entity.MenuItemEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * MenuItem Repository
 */
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Long> {

    /**
     * 메뉴와 옵션 그룹을 함께 조회
     * 
     * DISTINCT 추가로 MultipleBagFetchException 해결
     * 옵션 아이템은 LAZY 로딩으로 자동 조회됨
     * 
     * @param menuId 메뉴 ID
     * @return 메뉴 + 옵션 그룹
     */
    @Query("SELECT DISTINCT m FROM MenuItemEntity m " +
            "LEFT JOIN FETCH m.optionGroups " +
            "WHERE m.menuId = :menuId")
    Optional<MenuItemEntity> findByIdWithOptions(@Param("menuId") Long menuId);
}