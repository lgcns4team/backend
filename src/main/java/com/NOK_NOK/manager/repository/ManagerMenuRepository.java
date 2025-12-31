package com.NOK_NOK.manager.repository;

import com.NOK_NOK.order.domain.entity.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 매니저 메뉴 관리 Repository
 */
@Repository
public interface ManagerMenuRepository extends JpaRepository<MenuItemEntity, Long> {

    /**
     * 전체 메뉴 목록 조회 (카테고리 포함)
     * 카테고리 표시 순서대로 정렬
     */
    @Query("SELECT m FROM MenuItemEntity m " +
            "JOIN FETCH m.category c " +
            "ORDER BY c.displayOrder ASC, m.menuId ASC")
    List<MenuItemEntity> findAllMenusWithCategory();
}