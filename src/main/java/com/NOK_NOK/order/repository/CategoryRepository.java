package com.NOK_NOK.order.repository;

import com.NOK_NOK.order.domain.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    
    /**
     * display_order 기준 정렬된 카테고리 목록 조회
     */
    List<CategoryEntity> findAllByOrderByDisplayOrderAsc();
}
