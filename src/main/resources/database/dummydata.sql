
-- ============================================
-- 1. 카테고리
-- ============================================
INSERT INTO category (category_id, name, display_order) VALUES
(1, '커피', 1),
(2, '논커피', 2),
(3, '디저트', 3);

-- ============================================
-- 2. 매장
-- ============================================
INSERT INTO store (store_id, name, code, address, is_active) VALUES
(1, '강남 1호점', 'GN01', '서울시 강남구 어딘가 1길 10', 1),
(2, '홍대 1호점', 'HD01', '서울시 마포구 홍대입구로 20', 1);

-- ============================================
-- 3. 키오스크
-- ============================================
INSERT INTO kiosk (kiosk_id, store_id, kiosk_code, password, location, is_active) VALUES
(1, 1, 'GN01-K01', 'pw1', '입구', 1),
(2, 1, 'GN01-K02', 'pw2', '창가', 1),
(3, 2, 'HD01-K01', 'pw3', '입구', 1);

-- ============================================
-- 4. 관리자
-- ============================================
INSERT INTO admin_user (admin_id, store_id, username, password, is_active) VALUES
(1, 1, 'gn_admin', 'adminpw1', 1),
(2, 2, 'hd_admin', 'adminpw2', 1);

-- ============================================
-- 5. 메뉴 아이템
-- ============================================
INSERT INTO menu_item (menu_id, category_id, name, price, is_active, image_url) VALUES
(1, 1, '아메리카노', 4000, 1, '/images/menu/americano.png'),
(2, 1, '카페라떼', 4500, 1, '/images/menu/cafe_latte.png'),
(3, 2, '레몬에이드', 5000, 1, '/images/menu/lemon_ade.png'),
(4, 3, '치즈케이크', 5500, 1, '/images/menu/cheese_cake.png');

-- ============================================
-- 6. 옵션 그룹
-- ============================================
INSERT INTO option_group (option_group_id, menu_id, name, is_required, selection_type) VALUES
(1, 1, '사이즈', 1, 'SINGLE'),
(2, 1, '얼음량', 0, 'SINGLE'),
(3, 1, '샷 추가', 0, 'MULTI'),
(4, 2, '사이즈', 1, 'SINGLE'),
(5, 3, '사이즈', 1, 'SINGLE');

-- ============================================
-- 7. 옵션 아이템
-- ============================================
INSERT INTO option_item (option_item_id, option_group_id, name, option_price) VALUES
(1, 1, 'Regular', 0),
(2, 1, 'Large', 500),
(3, 2, '얼음 적게', 0),
(4, 2, '얼음 보통', 0),
(5, 2, '얼음 많이', 0),
(6, 3, '샷 1개 추가', 500),
(7, 3, '샷 2개 추가', 1000),
(8, 4, 'Regular', 0),
(9, 4, 'Large', 500),
(10, 5, 'Regular', 0),
(11, 5, 'Large', 500);

-- ============================================
-- 8. 고객 세션
-- ============================================
INSERT INTO customer_session (session_id, store_id, created_at, ended_at, age_group, gender, is_senior_mode) VALUES
(1, 1, NOW() - INTERVAL 30 MINUTE, NOW() - INTERVAL 25 MINUTE, '20대', 'M', 0),
(2, 1, NOW() - INTERVAL 20 MINUTE, NOW() - INTERVAL 15 MINUTE, '60대이상', 'F', 1),
(3, 2, NOW() - INTERVAL 10 MINUTE, NOW() - INTERVAL 5 MINUTE, '30대', 'F', 0);

-- ============================================
-- 9. 주문
-- ============================================
INSERT INTO orders (order_id, session_id, store_id, order_no, status, total_amount, created_at, paid_at, payment_method, payment_status, pg_transaction_id) VALUES
(1, 1, 1, 1001, 1, 4500, NOW() - INTERVAL 29 MINUTE, NOW() - INTERVAL 28 MINUTE, 'CARD', 'PAID', 'PG-20251202-0001'),
(2, 2, 1, 1002, 1, 9500, NOW() - INTERVAL 19 MINUTE, NOW() - INTERVAL 18 MINUTE, 'CARD', 'PAID', 'PG-20251202-0002'),
(3, 3, 2, 2001, 0, 5000, NOW() - INTERVAL 9 MINUTE, NULL, 'CARD', 'CANCEL', 'PG-20251202-0003');

-- ============================================
-- 10. 주문 아이템
-- ============================================
INSERT INTO order_item (order_item_id, order_id, menu_id, quantity, unit_price, line_amount) VALUES
(1, 1, 1, 1, 4500, 4500),
(2, 2, 1, 1, 4000, 4000),
(3, 2, 4, 1, 5500, 5500),
(4, 3, 3, 1, 5000, 5000);

-- ============================================
-- 11. 주문 옵션
-- ============================================
INSERT INTO order_item_option (order_item_option_id, order_item_id, option_item_id, extra_price) VALUES
(1, 1, 2, 500),
(2, 1, 6, 500),
(3, 2, 1, 0),
(4, 4, 11, 500);

-- ============================================
-- 12. 광고 컨텐츠
-- ============================================
INSERT INTO ad_content (ad_id, title, media_type, media_url, start_date, end_date, is_active) VALUES
(1, '점심 타임 아메리카노 20% 할인', 'IMAGE', '/ads/lunch_americano.png', '2025-12-01', '2025-12-31', 1),
(2, '시니어 전용 세트 메뉴', 'IMAGE', '/ads/senior_set.png', '2025-12-01', '2025-12-31', 1),
(3, '디저트 2+1 프로모션', 'IMAGE', '/ads/dessert_2plus1.png', '2025-12-01', '2025-12-15', 1);

-- ============================================
-- 13. 광고 타겟 룰
-- ============================================
INSERT INTO ad_target_rule (rule_id, ad_id, age_group, gender) VALUES
(1, 1, '20대', NULL),
(2, 1, '30대', NULL),
(3, 2, '60대이상', NULL),
(4, 3, '20대', 'F');

-- ============================================
-- 14. 광고 로그
-- ============================================
INSERT INTO ad_display_log (display_id, session_id, ad_id, displayed_at, duration_ms) VALUES
(1, 1, 1, NOW() - INTERVAL 29 MINUTE, 8000),
(2, 2, 2, NOW() - INTERVAL 19 MINUTE, 10000),
(3, 3, 3, NOW() - INTERVAL 9 MINUTE, 5000);
