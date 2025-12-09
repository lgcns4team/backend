package com.NOK_NOK.order.repository;

import com.NOK_NOK.order.domain.entity.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<MenuItem, Long> {

        /**
         * 카테고리별 메뉴 목록 조회 (페이징)
         */
        Page<MenuItem> findByCategoryCategoryId(Long categoryId, Pageable pageable);

        /**
         * 전체 메뉴 목록 조회 (페이징)
         */
        Page<MenuItem> findAll(Pageable pageable);

        /**
         * 메뉴명으로 검색 (페이징)
         */
        @Query("SELECT m FROM MenuItem m WHERE m.name LIKE %:keyword%")
        Page<MenuItem> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

        /**
         * 카테고리 + 메뉴명으로 검색 (페이징)
         */
        @Query("SELECT m FROM MenuItem m WHERE m.category.categoryId = :categoryId AND m.name LIKE %:keyword%")
        Page<MenuItem> searchByCategoryAndKeyword(
                        @Param("categoryId") Long categoryId,
                        @Param("keyword") String keyword,
                        Pageable pageable);

        /**
         * 메뉴 ID로 옵션 그룹과 함께 조회 (N+1 문제 해결)
         */
        // @Query("SELECT m FROM MenuItem m " +
                        // "LEFT JOIN FETCH m.category " +
                        // "LEFT JOIN FETCH m.optionGroups og " +
                        // "LEFT JOIN FETCH og.optionItems " +
                        // "WHERE m.menuId = :menuId")
        // Optional<MenuItem> findByIdWithOptions(@Param("menuId") Long menuId);
        
        Optional<MenuItem> findById(Long menuId);

        /**
         * 카테고리 내 메뉴명 중복 체크
         */
        boolean existsByCategoryCategoryIdAndName(Long categoryId, String name);

        /**
         * 특정 메뉴 ID가 아닌 카테고리 내 같은 이름 존재 여부
         */
        @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MenuItem m WHERE m.category.categoryId = :categoryId AND m.name = :name AND m.menuId <> :menuId")
        boolean existsByCategoryIdAndNameAndMenuIdNot(
                        @Param("categoryId") Long categoryId,
                        @Param("name") String name,
                        @Param("menuId") Long menuId);

        /**
         * 추천 메뉴 조회 (추후 추천 로직 구현)
         * TODO: 연령대, 성별, 시간대 기반 추천 로직 구현
         */
        @Query("SELECT m FROM MenuItem m ORDER BY m.menuId DESC")
        List<MenuItem> findRecommendedMenus(Pageable pageable);
}
