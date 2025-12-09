package com.NOK_NOK.order.repository;

import com.NOK_NOK.order.domain.entity.OptionItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * OptionItem Repository
 */
@Repository
public interface OptionItemRepository extends JpaRepository<OptionItemEntity, Long> {

    /**
     * 여러 옵션을 한 번에 조회 (옵션 그룹 포함)
     * 
     * @param optionItemIds 옵션 ID 목록
     * @return 옵션 아이템 목록 (옵션 그룹 포함)
     */
    @Query("SELECT oi FROM OptionItemEntity oi " +
            "JOIN FETCH oi.optionGroup " +
            "WHERE oi.optionItemId IN :optionItemIds")
    List<OptionItemEntity> findByIdInWithGroup(@Param("optionItemIds") List<Long> optionItemIds);
}
