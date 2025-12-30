package com.NOK_NOK.abs.repository;

import com.NOK_NOK.abs.domain.entity.AdContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * AdContent Repository
 */
@Repository
public interface AdContentRepository extends JpaRepository<AdContentEntity, Long> {

    /**
     * 활성화된 모든 광고 조회
     * 
     * @param today 오늘 날짜
     * @return 활성화된 광고 목록
     */
    @Query("SELECT ac FROM AdContentEntity ac " +
           "WHERE ac.isActive = true " +
           "AND (ac.startDate IS NULL OR ac.startDate <= :today) " +
           "AND (ac.endDate IS NULL OR ac.endDate >= :today)")
    List<AdContentEntity> findAllActiveAds(@Param("today") LocalDate today);
}