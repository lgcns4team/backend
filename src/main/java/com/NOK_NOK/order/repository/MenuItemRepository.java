package com.NOK_NOK.order.repository;

import com.NOK_NOK.order.domain.entity.MenuItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Long> {

    /**
     * 카테고리별 메뉴 조회 (페이지네이션)
     */
    Page<MenuItemEntity> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * 카테고리 + 활성화 상태별 메뉴 조회
     */
    Page<MenuItemEntity> findByCategoryIdAndIsActive(Long categoryId, Boolean isActive, Pageable pageable);

    /**
     * 키워드 검색 (메뉴명)
     */
    @Query("SELECT m FROM MenuItem m WHERE " +
            "(:categoryId IS NULL OR m.categoryId = :categoryId) AND " +
            "(:isActive IS NULL OR m.isActive = :isActive) AND " +
            "(:keyword IS NULL OR m.name LIKE %:keyword%)")
    Page<MenuItemEntity> searchMenus(
            @Param("categoryId") Long categoryId,
            @Param("isActive") Boolean isActive,
            @Param("keyword") String keyword,
            Pageable pageable);
}
