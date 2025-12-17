package com.NOK_NOK.order.repository;

import com.NOK_NOK.order.domain.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Store Repository
 */
@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    
}
