package com.NOK_NOK.abs.repository;

import com.NOK_NOK.abs.domain.entity.AdDisplayLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * AdDisplayLog Repository
 */
@Repository
public interface AdDisplayLogRepository extends JpaRepository<AdDisplayLogEntity, Long> {
    
}