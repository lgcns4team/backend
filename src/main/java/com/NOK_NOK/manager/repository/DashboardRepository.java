package com.NOK_NOK.manager.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 대시보드 데이터 조회를 위한 Repository (SQL 문법 오류 수정)
 * JdbcTemplate을 사용한 Native Query 기반 구현
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class DashboardRepository {

    private final JdbcTemplate jdbcTemplate;

    // ===== 핵심 지표 조회 =====

    /**
     * 기간별 총 매출 조회
     */
    public Long getTotalSales(LocalDate startDate, LocalDate endDate, Long storeId) {
        String sql = """
            SELECT COALESCE(SUM(o.total_amount), 0) as total_sales
            FROM orders o
            WHERE DATE(o.created_at) BETWEEN ? AND ?
            AND o.status = 1
            """ + (storeId != null ? "AND o.store_id = ? " : "");

        if (storeId != null) {
            return jdbcTemplate.queryForObject(sql, Long.class, startDate, endDate, storeId);
        } else {
            return jdbcTemplate.queryForObject(sql, Long.class, startDate, endDate);
        }
    }

    /**
     * 기간별 총 주문 건수 조회
     */
    public Integer getTotalOrders(LocalDate startDate, LocalDate endDate, Long storeId) {
        String sql = """
            SELECT COUNT(*) as total_orders
            FROM orders o
            WHERE DATE(o.created_at) BETWEEN ? AND ?
            AND o.status = 1
            """ + (storeId != null ? "AND o.store_id = ? " : "");

        if (storeId != null) {
            return jdbcTemplate.queryForObject(sql, Integer.class, startDate, endDate, storeId);
        } else {
            return jdbcTemplate.queryForObject(sql, Integer.class, startDate, endDate);
        }
    }

    /**
     * 기간별 총 고객 수 조회 (세션 수)
     */
    public Integer getTotalCustomers(LocalDate startDate, LocalDate endDate, Long storeId) {
        String sql = """
            SELECT COUNT(DISTINCT cs.session_id) as total_customers
            FROM customer_session cs
            WHERE DATE(cs.created_at) BETWEEN ? AND ?
            AND cs.ended_at IS NOT NULL
            """ + (storeId != null ? "AND cs.store_id = ? " : "");

        if (storeId != null) {
            return jdbcTemplate.queryForObject(sql, Integer.class, startDate, endDate, storeId);
        } else {
            return jdbcTemplate.queryForObject(sql, Integer.class, startDate, endDate);
        }
    }

    /**
     * 기간별 시니어 모드 사용자 수 조회
     */
    public Integer getSeniorModeUsers(LocalDate startDate, LocalDate endDate, Long storeId) {
        String sql = """
            SELECT COUNT(*) as senior_users
            FROM customer_session cs
            WHERE DATE(cs.created_at) BETWEEN ? AND ?
            AND cs.ended_at IS NOT NULL
            AND cs.is_senior_mode = 1
            """ + (storeId != null ? "AND cs.store_id = ? " : "");

        if (storeId != null) {
            return jdbcTemplate.queryForObject(sql, Integer.class, startDate, endDate, storeId);
        } else {
            return jdbcTemplate.queryForObject(sql, Integer.class, startDate, endDate);
        }
    }

    // ===== 인기메뉴 조회 =====

    /**
     * 기간별 인기메뉴 조회
     */
    public List<Map<String, Object>> getPopularMenus(LocalDate startDate, LocalDate endDate, Long storeId, int limit) {
        String sql = """
            SELECT 
                m.menu_id as menuId,
                m.name as menuName,
                c.name as categoryName,
                COUNT(DISTINCT o.order_id) as orderCount,
                SUM(oi.quantity) as totalQuantity,
                SUM(oi.line_amount) as totalSales,
                m.image_url as imageUrl
            FROM menu_item m
            JOIN category c ON m.category_id = c.category_id
            JOIN order_item oi ON m.menu_id = oi.menu_id
            JOIN orders o ON oi.order_id = o.order_id
            WHERE DATE(o.created_at) BETWEEN ? AND ?
            AND o.status = 1
            AND m.is_active = 1
            """ + (storeId != null ? "AND o.store_id = ? " : "") + """
            GROUP BY m.menu_id, m.name, c.name, m.image_url
            ORDER BY orderCount DESC, totalQuantity DESC, totalSales DESC
            LIMIT ?
            """;

        if (storeId != null) {
            return jdbcTemplate.queryForList(sql, startDate, endDate, storeId, limit);
        } else {
            return jdbcTemplate.queryForList(sql, startDate, endDate, limit);
        }
    }

    // ===== 시간대별/일간 매출 =====

    /**
     * 기간별 시간대별 매출 조회
     */
    public List<Map<String, Object>> getHourlySales(LocalDate startDate, LocalDate endDate, Long storeId) {
        String sql = """
            SELECT 
                HOUR(o.created_at) as hour,
                COALESCE(SUM(o.total_amount), 0) as amount,
                COUNT(*) as orderCount
            FROM orders o
            WHERE DATE(o.created_at) BETWEEN ? AND ?
            AND o.status = 1
            """ + (storeId != null ? "AND o.store_id = ? " : "") + """
            GROUP BY HOUR(o.created_at)
            ORDER BY hour
            """;

        if (storeId != null) {
            return jdbcTemplate.queryForList(sql, startDate, endDate, storeId);
        } else {
            return jdbcTemplate.queryForList(sql, startDate, endDate);
        }
    }

    /**
     * 기간별 일간 매출 조회
     */
    public List<Map<String, Object>> getDailySales(LocalDate startDate, LocalDate endDate, Long storeId) {
        String sql = """
            SELECT 
                DATE(o.created_at) as saleDate,
                COALESCE(SUM(o.total_amount), 0) as amount,
                COUNT(*) as orderCount,
                COUNT(DISTINCT o.session_id) as customerCount
            FROM orders o
            WHERE DATE(o.created_at) BETWEEN ? AND ?
            AND o.status = 1
            """ + (storeId != null ? "AND o.store_id = ? " : "") + """
            GROUP BY DATE(o.created_at)
            ORDER BY saleDate ASC
            """;

        if (storeId != null) {
            return jdbcTemplate.queryForList(sql, startDate, endDate, storeId);
        } else {
            return jdbcTemplate.queryForList(sql, startDate, endDate);
        }
    }

    // ===== 고객 성별/연령대 분석 =====

    /**
     * 기간별 성별 통계 조회
     */
    public Map<String, Object> getGenderStats(LocalDate startDate, LocalDate endDate, Long storeId) {
        String sql = """
            SELECT 
                SUM(CASE WHEN cs.gender = 'M' THEN 1 ELSE 0 END) as maleCount,
                SUM(CASE WHEN cs.gender = 'F' THEN 1 ELSE 0 END) as femaleCount,
                COUNT(*) as totalCount
            FROM customer_session cs
            WHERE DATE(cs.created_at) BETWEEN ? AND ?
            AND cs.ended_at IS NOT NULL
            """ + (storeId != null ? "AND cs.store_id = ? " : "");

        if (storeId != null) {
            return jdbcTemplate.queryForMap(sql, startDate, endDate, storeId);
        } else {
            return jdbcTemplate.queryForMap(sql, startDate, endDate);
        }
    }

    /**
     * 기간별 연령대별 통계 조회 (SQL 문법 오류 수정)
     */
    public List<Map<String, Object>> getAgeGroupStats(LocalDate startDate, LocalDate endDate, Long storeId) {
        String sql = """
            SELECT 
                cs.age_group as ageGroup,
                COUNT(*) as customerCount
            FROM customer_session cs
            WHERE DATE(cs.created_at) BETWEEN ? AND ?
            AND cs.ended_at IS NOT NULL
            AND cs.age_group IS NOT NULL
            """ + (storeId != null ? "AND cs.store_id = ? " : "") + """
            GROUP BY cs.age_group
            ORDER BY 
                CASE cs.age_group
                    WHEN '10대' THEN 1
                    WHEN '20대' THEN 2
                    WHEN '30대' THEN 3
                    WHEN '40대' THEN 4
                    WHEN '50대' THEN 5
                    WHEN '60대이상' THEN 6
                    ELSE 7
                END
            """;

        if (storeId != null) {
            return jdbcTemplate.queryForList(sql, startDate, endDate, storeId);
        } else {
            return jdbcTemplate.queryForList(sql, startDate, endDate);
        }
    }

    /**
     * 기간별 시간대별 고객 성별/연령대 분석 (선택사항)
     */
    public List<Map<String, Object>> getCustomerDemographicsByTimeSlot(LocalDate startDate, LocalDate endDate, Long storeId) {
        String sql = """
            SELECT 
                HOUR(cs.created_at) as hour,
                cs.age_group as ageGroup,
                cs.gender as gender,
                COUNT(*) as customerCount
            FROM customer_session cs
            WHERE DATE(cs.created_at) BETWEEN ? AND ?
            AND cs.ended_at IS NOT NULL
            """ + (storeId != null ? "AND cs.store_id = ? " : "") + """
            GROUP BY HOUR(cs.created_at), cs.age_group, cs.gender
            ORDER BY hour, cs.age_group, cs.gender
            """;

        if (storeId != null) {
            return jdbcTemplate.queryForList(sql, startDate, endDate, storeId);
        } else {
            return jdbcTemplate.queryForList(sql, startDate, endDate);
        }
    }
}