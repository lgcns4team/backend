package com.NOK_NOK.order.repository;

import com.NOK_NOK.order.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 모든 카테고리 조회 (표시 순서로 정렬)
     */
    List<Category> findAllByOrderByDisplayOrderAsc();

    /**
     * 카테고리명으로 검색
     */
    @Query("SELECT c FROM Category c WHERE c.name LIKE %:keyword% ORDER BY c.displayOrder ASC")
    List<Category> findByNameContaining(@Param("keyword") String keyword);

    /**
     * 카테고리 ID로 메뉴와 함께 조회 (N+1 문제 해결)
     */
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.menuItems WHERE c.categoryId = :categoryId")
    Optional<Category> findByIdWithMenus(@Param("categoryId") Long categoryId);

    /**
     * 카테고리명 중복 체크
     */
    boolean existsByName(String name);

    /**
     * 특정 ID가 아닌 카테고리 중 같은 이름 존재 여부
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Category c WHERE c.name = :name AND c.categoryId <> :categoryId")
    boolean existsByNameAndCategoryIdNot(@Param("name") String name, @Param("categoryId") Long categoryId);
}
