package com.NOK_NOK.abs.repository;

import com.NOK_NOK.abs.domain.entity.AdTargetRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AdTargetRule Repository
 */
@Repository
public interface AdTargetRuleRepository extends JpaRepository<AdTargetRuleEntity, Long> {

    /**
     * 특정 광고의 타겟 규칙 조회
     * 
     * @param adId 광고 ID
     * @return 타겟 규칙 목록
     */
    @Query("SELECT atr FROM AdTargetRuleEntity atr " +
           "WHERE atr.adContent.adId = :adId")
    List<AdTargetRuleEntity> findByAdId(@Param("adId") Long adId);

    /**
     * 광고 ID 목록으로 타겟 규칙 일괄 조회
     * 
     * @param adIds 광고 ID 목록
     * @return 타겟 규칙 목록
     */
    @Query("SELECT atr FROM AdTargetRuleEntity atr " +
           "WHERE atr.adContent.adId IN :adIds")
    List<AdTargetRuleEntity> findByAdIdIn(@Param("adIds") List<Long> adIds);
}