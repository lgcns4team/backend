package com.NOK_NOK.order.repository;

import com.NOK_NOK.order.domain.entity.CustomerSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * CustomerSession Repository
 */
@Repository
public interface CustomerSessionRepository extends JpaRepository<CustomerSessionEntity, Long> {

    /**
     * 세션 ID로 세션 조회 (매장 정보 포함)
     * 
     * @param sessionId 세션 ID
     * @return 고객 세션 엔티티
     */
    @Query("SELECT cs FROM CustomerSessionEntity cs " +
           "JOIN FETCH cs.store " +
           "WHERE cs.sessionId = :sessionId")
    Optional<CustomerSessionEntity> findByIdWithStore(@Param("sessionId") Long sessionId);
}
