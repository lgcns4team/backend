
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



-- -----------------------

-- ============================================
-- 기존 데이터 전체 삭제 및 신규 데이터 입력
-- ============================================

SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 기존 데이터 삭제 (역순으로 삭제)
-- ============================================
TRUNCATE TABLE ad_display_log;
TRUNCATE TABLE ad_target_rule;
TRUNCATE TABLE ad_content;
TRUNCATE TABLE order_item_option;
TRUNCATE TABLE order_item;
TRUNCATE TABLE orders;
TRUNCATE TABLE customer_session;
TRUNCATE TABLE option_item;
TRUNCATE TABLE option_group;
TRUNCATE TABLE menu_item;
TRUNCATE TABLE admin_user;
TRUNCATE TABLE kiosk;
TRUNCATE TABLE store;
TRUNCATE TABLE category;

-- ============================================
-- 1. 카테고리 (Category)
-- ============================================
INSERT INTO category (category_id, name, display_order) VALUES
(1, '커피', 1),
(2, '음료', 2),
(3, '디저트', 3),
(4, '브런치', 4),
(5, '베이커리', 5);

-- ============================================
-- 2. 매장 (Store) - 5개 매장
-- ============================================
INSERT INTO store (store_id, name, code, address, is_active) VALUES
(1, '강남 1호점', 'GN01', '서울시 강남구 테헤란로 123', 1),
(2, '홍대 1호점', 'HD01', '서울시 마포구 홍대입구로 45', 1),
(3, '신촌 1호점', 'SC01', '서울시 서대문구 신촌역로 89', 1),
(4, '잠실 1호점', 'JS01', '서울시 송파구 올림픽로 567', 1),
(5, '명동 1호점', 'MD01', '서울시 중구 명동길 234', 1);

-- ============================================
-- 3. 키오스크 (Kiosk) - 매장당 2~3개
-- ============================================
INSERT INTO kiosk (kiosk_id, store_id, kiosk_code, password, location, is_active) VALUES
(1, 1, 'GN01-K01', 'pw1001', '입구', 1),
(2, 1, 'GN01-K02', 'pw1002', '창가', 1),
(3, 2, 'HD01-K01', 'pw2001', '입구', 1),
(4, 2, 'HD01-K02', 'pw2002', '내부', 1),
(5, 3, 'SC01-K01', 'pw3001', '입구', 1),
(6, 3, 'SC01-K02', 'pw3002', '2층', 1),
(7, 4, 'JS01-K01', 'pw4001', '입구', 1),
(8, 4, 'JS01-K02', 'pw4002', '푸드코트', 1),
(9, 4, 'JS01-K03', 'pw4003', '외부', 1),
(10, 5, 'MD01-K01', 'pw5001', '1층', 1),
(11, 5, 'MD01-K02', 'pw5002', '2층', 1);

-- ============================================
-- 4. 관리자 (Admin User) - 매장당 1~2명
-- ============================================
INSERT INTO admin_user (admin_id, store_id, username, password, is_active) VALUES
(1, 1, 'gn_admin', 'admin1234', 1),
(2, 1, 'gn_manager', 'manager1234', 1),
(3, 2, 'hd_admin', 'admin1234', 1),
(4, 3, 'sc_admin', 'admin1234', 1),
(5, 4, 'js_admin', 'admin1234', 1),
(6, 4, 'js_manager', 'manager1234', 1),
(7, 5, 'md_admin', 'admin1234', 1);

-- ============================================
-- 5. 메뉴 아이템 (Menu Item) - 카테고리별로 다양하게
-- ============================================
INSERT INTO menu_item (menu_id, category_id, name, price, is_active, image_url) VALUES
-- 커피 (15개)
(1, 1, '아메리카노', 4000, 1, '/images/menu/americano.png'),
(2, 1, '카페라떼', 4500, 1, '/images/menu/cafe_latte.png'),
(3, 1, '카푸치노', 4500, 1, '/images/menu/cappuccino.png'),
(4, 1, '카라멜 마키아또', 5000, 1, '/images/menu/caramel_macchiato.png'),
(5, 1, '바닐라 라떼', 5000, 1, '/images/menu/vanilla_latte.png'),
(6, 1, '카페모카', 5000, 1, '/images/menu/cafe_mocha.png'),
(7, 1, '에스프레소', 3500, 1, '/images/menu/espresso.png'),
(8, 1, '콜드브루', 4500, 1, '/images/menu/coldbrew.png'),
(9, 1, '플랫화이트', 4800, 1, '/images/menu/flatwhite.png'),
(10, 1, '아인슈페너', 5500, 1, '/images/menu/einspanner.png'),
(11, 1, '헤이즐넛 라떼', 5200, 1, '/images/menu/hazelnut_latte.png'),
(12, 1, '연유 라떼', 5200, 1, '/images/menu/condensed_milk_latte.png'),
(13, 1, '디카페인 아메리카노', 4500, 1, '/images/menu/decaf_americano.png'),
(14, 1, '디카페인 라떼', 5000, 1, '/images/menu/decaf_latte.png'),
(15, 1, '더치커피', 5500, 1, '/images/menu/dutch_coffee.png'),

-- 음료 (12개)
(16, 2, '레몬에이드', 5000, 1, '/images/menu/lemon_ade.png'),
(17, 2, '자몽에이드', 5200, 1, '/images/menu/grapefruit_ade.png'),
(18, 2, '청포도에이드', 5200, 1, '/images/menu/green_grape_ade.png'),
(19, 2, '딸기라떼', 5500, 1, '/images/menu/strawberry_latte.png'),
(20, 2, '초코라떼', 5000, 1, '/images/menu/choco_latte.png'),
(21, 2, '녹차라떼', 5000, 1, '/images/menu/green_tea_latte.png'),
(22, 2, '흑당라떼', 5500, 1, '/images/menu/brown_sugar_latte.png'),
(23, 2, '밀크티', 4800, 1, '/images/menu/milk_tea.png'),
(24, 2, '유자차', 4500, 1, '/images/menu/citron_tea.png'),
(25, 2, '캐모마일', 4500, 1, '/images/menu/chamomile.png'),
(26, 2, '페퍼민트', 4500, 1, '/images/menu/peppermint.png'),
(27, 2, '얼그레이', 4500, 1, '/images/menu/earl_grey.png'),

-- 디저트 (10개)
(28, 3, '치즈케이크', 5500, 1, '/images/menu/cheese_cake.png'),
(29, 3, '티라미수', 6000, 1, '/images/menu/tiramisu.png'),
(30, 3, '초코 브라우니', 4500, 1, '/images/menu/choco_brownie.png'),
(31, 3, '마카롱 세트', 7000, 1, '/images/menu/macaron_set.png'),
(32, 3, '휘낭시에', 3500, 1, '/images/menu/financier.png'),
(33, 3, '마들렌', 3500, 1, '/images/menu/madeleine.png'),
(34, 3, '쿠키 세트', 5000, 1, '/images/menu/cookie_set.png'),
(35, 3, '푸딩', 4000, 1, '/images/menu/pudding.png'),
(36, 3, '스콘', 4000, 1, '/images/menu/scone.png'),
(37, 3, '아이스크림', 3500, 1, '/images/menu/icecream.png'),

-- 브런치 (8개)
(38, 4, '크로와상 샌드위치', 6500, 1, '/images/menu/croissant_sandwich.png'),
(39, 4, '베이글 샌드위치', 6000, 1, '/images/menu/bagel_sandwich.png'),
(40, 4, '에그 베네딕트', 8500, 1, '/images/menu/egg_benedict.png'),
(41, 4, '팬케이크', 7500, 1, '/images/menu/pancake.png'),
(42, 4, '와플', 7000, 1, '/images/menu/waffle.png'),
(43, 4, '프렌치토스트', 7500, 1, '/images/menu/french_toast.png'),
(44, 4, '샐러드', 8000, 1, '/images/menu/salad.png'),
(45, 4, '아보카도 토스트', 8500, 1, '/images/menu/avocado_toast.png'),

-- 베이커리 (10개)
(46, 5, '크로와상', 3500, 1, '/images/menu/croissant.png'),
(47, 5, '베이글', 3000, 1, '/images/menu/bagel.png'),
(48, 5, '소금빵', 3500, 1, '/images/menu/salt_bread.png'),
(49, 5, '모닝빵', 2500, 1, '/images/menu/morning_bread.png'),
(50, 5, '단팥빵', 3000, 1, '/images/menu/red_bean_bread.png'),
(51, 5, '크림빵', 3000, 1, '/images/menu/cream_bread.png'),
(52, 5, '식빵', 4500, 1, '/images/menu/white_bread.png'),
(53, 5, '바게트', 4000, 1, '/images/menu/baguette.png'),
(54, 5, '치아바타', 4000, 1, '/images/menu/ciabatta.png'),
(55, 5, '호밀빵', 4500, 1, '/images/menu/rye_bread.png');

-- ============================================
-- 6. 옵션 그룹 (Option Group) - 주요 메뉴에 대해
-- ============================================
INSERT INTO option_group (option_group_id, menu_id, name, is_required, selection_type) VALUES
-- 아메리카노 옵션
(1, 1, '사이즈', 1, 'SINGLE'),
(2, 1, '얼음량', 0, 'SINGLE'),
(3, 1, '샷 추가', 0, 'MULTI'),

-- 카페라떼 옵션
(4, 2, '사이즈', 1, 'SINGLE'),
(5, 2, '온도', 1, 'SINGLE'),
(6, 2, '샷 추가', 0, 'MULTI'),
(7, 2, '시럽 추가', 0, 'MULTI'),

-- 카푸치노 옵션
(8, 3, '사이즈', 1, 'SINGLE'),
(9, 3, '샷 추가', 0, 'MULTI'),

-- 카라멜 마키아또 옵션
(10, 4, '사이즈', 1, 'SINGLE'),
(11, 4, '온도', 1, 'SINGLE'),

-- 레몬에이드 옵션
(12, 16, '사이즈', 1, 'SINGLE'),
(13, 16, '얼음량', 0, 'SINGLE'),

-- 자몽에이드 옵션
(14, 17, '사이즈', 1, 'SINGLE'),

-- 딸기라떼 옵션
(15, 19, '사이즈', 1, 'SINGLE'),
(16, 19, '온도', 1, 'SINGLE'),

-- 샌드위치 옵션
(17, 38, '빵 종류', 1, 'SINGLE'),
(18, 39, '빵 종류', 1, 'SINGLE'),

-- 팬케이크 옵션
(19, 41, '토핑', 0, 'MULTI');

-- ============================================
-- 7. 옵션 아이템 (Option Item)
-- ============================================
INSERT INTO option_item (option_item_id, option_group_id, name, option_price) VALUES
-- 사이즈 (공통)
(1, 1, 'Regular', 0),
(2, 1, 'Large', 500),
(3, 4, 'Regular', 0),
(4, 4, 'Large', 500),
(5, 8, 'Regular', 0),
(6, 8, 'Large', 500),
(7, 10, 'Regular', 0),
(8, 10, 'Large', 500),
(9, 12, 'Regular', 0),
(10, 12, 'Large', 500),
(11, 14, 'Regular', 0),
(12, 14, 'Large', 500),
(13, 15, 'Regular', 0),
(14, 15, 'Large', 500),

-- 얼음량
(15, 2, '얼음 적게', 0),
(16, 2, '얼음 보통', 0),
(17, 2, '얼음 많이', 0),
(18, 13, '얼음 적게', 0),
(19, 13, '얼음 보통', 0),
(20, 13, '얼음 많이', 0),

-- 샷 추가
(21, 3, '샷 1개 추가', 500),
(22, 3, '샷 2개 추가', 1000),
(23, 6, '샷 1개 추가', 500),
(24, 6, '샷 2개 추가', 1000),
(25, 9, '샷 1개 추가', 500),
(26, 9, '샷 2개 추가', 1000),

-- 온도
(27, 5, 'HOT', 0),
(28, 5, 'ICED', 0),
(29, 11, 'HOT', 0),
(30, 11, 'ICED', 0),
(31, 16, 'HOT', 0),
(32, 16, 'ICED', 0),

-- 시럽 추가
(33, 7, '바닐라 시럽', 500),
(34, 7, '카라멜 시럽', 500),
(35, 7, '헤이즐넛 시럽', 500),

-- 빵 종류
(36, 17, '크로와상', 0),
(37, 17, '통밀빵', 500),
(38, 18, '플레인 베이글', 0),
(39, 18, '어니언 베이글', 500),

-- 토핑
(40, 19, '메이플 시럽', 0),
(41, 19, '생크림', 1000),
(42, 19, '과일', 1500);

-- ============================================
-- 8. 고객 세션 (Customer Session) - 50개
-- ============================================
INSERT INTO customer_session (session_id, store_id, created_at, ended_at, age_group, gender, is_senior_mode) VALUES
(1, 1, NOW() - INTERVAL 240 MINUTE, NOW() - INTERVAL 235 MINUTE, '20대', 'F', 0),
(2, 1, NOW() - INTERVAL 235 MINUTE, NOW() - INTERVAL 230 MINUTE, '30대', 'M', 0),
(3, 2, NOW() - INTERVAL 230 MINUTE, NOW() - INTERVAL 225 MINUTE, '40대', 'F', 0),
(4, 1, NOW() - INTERVAL 225 MINUTE, NOW() - INTERVAL 220 MINUTE, '60대이상', 'M', 1),
(5, 2, NOW() - INTERVAL 220 MINUTE, NOW() - INTERVAL 215 MINUTE, '20대', 'M', 0),
(6, 3, NOW() - INTERVAL 215 MINUTE, NOW() - INTERVAL 210 MINUTE, '30대', 'F', 0),
(7, 1, NOW() - INTERVAL 210 MINUTE, NOW() - INTERVAL 205 MINUTE, '50대', 'M', 0),
(8, 2, NOW() - INTERVAL 205 MINUTE, NOW() - INTERVAL 200 MINUTE, '20대', 'F', 0),
(9, 3, NOW() - INTERVAL 200 MINUTE, NOW() - INTERVAL 195 MINUTE, '60대이상', 'F', 1),
(10, 4, NOW() - INTERVAL 195 MINUTE, NOW() - INTERVAL 190 MINUTE, '30대', 'M', 0),
(11, 2, NOW() - INTERVAL 190 MINUTE, NOW() - INTERVAL 185 MINUTE, '40대', 'M', 0),
(12, 3, NOW() - INTERVAL 185 MINUTE, NOW() - INTERVAL 180 MINUTE, '20대', 'M', 0),
(13, 4, NOW() - INTERVAL 180 MINUTE, NOW() - INTERVAL 175 MINUTE, '30대', 'F', 0),
(14, 5, NOW() - INTERVAL 175 MINUTE, NOW() - INTERVAL 170 MINUTE, '50대', 'F', 0),
(15, 1, NOW() - INTERVAL 170 MINUTE, NOW() - INTERVAL 165 MINUTE, '60대이상', 'M', 1),
(16, 2, NOW() - INTERVAL 165 MINUTE, NOW() - INTERVAL 160 MINUTE, '20대', 'F', 0),
(17, 3, NOW() - INTERVAL 160 MINUTE, NOW() - INTERVAL 155 MINUTE, '30대', 'M', 0),
(18, 4, NOW() - INTERVAL 155 MINUTE, NOW() - INTERVAL 150 MINUTE, '40대', 'F', 0),
(19, 5, NOW() - INTERVAL 150 MINUTE, NOW() - INTERVAL 145 MINUTE, '20대', 'M', 0),
(20, 1, NOW() - INTERVAL 145 MINUTE, NOW() - INTERVAL 140 MINUTE, '60대이상', 'F', 1),
(21, 2, NOW() - INTERVAL 140 MINUTE, NOW() - INTERVAL 135 MINUTE, '30대', 'F', 0),
(22, 3, NOW() - INTERVAL 135 MINUTE, NOW() - INTERVAL 130 MINUTE, '50대', 'M', 0),
(23, 4, NOW() - INTERVAL 130 MINUTE, NOW() - INTERVAL 125 MINUTE, '20대', 'F', 0),
(24, 5, NOW() - INTERVAL 125 MINUTE, NOW() - INTERVAL 120 MINUTE, '30대', 'M', 0),
(25, 1, NOW() - INTERVAL 120 MINUTE, NOW() - INTERVAL 115 MINUTE, '40대', 'F', 0),
(26, 2, NOW() - INTERVAL 115 MINUTE, NOW() - INTERVAL 110 MINUTE, '20대', 'M', 0),
(27, 3, NOW() - INTERVAL 110 MINUTE, NOW() - INTERVAL 105 MINUTE, '60대이상', 'M', 1),
(28, 4, NOW() - INTERVAL 105 MINUTE, NOW() - INTERVAL 100 MINUTE, '30대', 'F', 0),
(29, 5, NOW() - INTERVAL 100 MINUTE, NOW() - INTERVAL 95 MINUTE, '50대', 'F', 0),
(30, 1, NOW() - INTERVAL 95 MINUTE, NOW() - INTERVAL 90 MINUTE, '20대', 'F', 0),
(31, 2, NOW() - INTERVAL 90 MINUTE, NOW() - INTERVAL 85 MINUTE, '30대', 'M', 0),
(32, 3, NOW() - INTERVAL 85 MINUTE, NOW() - INTERVAL 80 MINUTE, '40대', 'M', 0),
(33, 4, NOW() - INTERVAL 80 MINUTE, NOW() - INTERVAL 75 MINUTE, '20대', 'F', 0),
(34, 5, NOW() - INTERVAL 75 MINUTE, NOW() - INTERVAL 70 MINUTE, '60대이상', 'F', 1),
(35, 1, NOW() - INTERVAL 70 MINUTE, NOW() - INTERVAL 65 MINUTE, '30대', 'M', 0),
(36, 2, NOW() - INTERVAL 65 MINUTE, NOW() - INTERVAL 60 MINUTE, '50대', 'M', 0),
(37, 3, NOW() - INTERVAL 60 MINUTE, NOW() - INTERVAL 55 MINUTE, '20대', 'M', 0),
(38, 4, NOW() - INTERVAL 55 MINUTE, NOW() - INTERVAL 50 MINUTE, '30대', 'F', 0),
(39, 5, NOW() - INTERVAL 50 MINUTE, NOW() - INTERVAL 45 MINUTE, '40대', 'F', 0),
(40, 1, NOW() - INTERVAL 45 MINUTE, NOW() - INTERVAL 40 MINUTE, '20대', 'F', 0),
(41, 2, NOW() - INTERVAL 40 MINUTE, NOW() - INTERVAL 35 MINUTE, '60대이상', 'M', 1),
(42, 3, NOW() - INTERVAL 35 MINUTE, NOW() - INTERVAL 30 MINUTE, '30대', 'M', 0),
(43, 4, NOW() - INTERVAL 30 MINUTE, NOW() - INTERVAL 25 MINUTE, '50대', 'F', 0),
(44, 5, NOW() - INTERVAL 25 MINUTE, NOW() - INTERVAL 20 MINUTE, '20대', 'M', 0),
(45, 1, NOW() - INTERVAL 20 MINUTE, NOW() - INTERVAL 15 MINUTE, '30대', 'F', 0),
(46, 2, NOW() - INTERVAL 15 MINUTE, NOW() - INTERVAL 10 MINUTE, '40대', 'M', 0),
(47, 3, NOW() - INTERVAL 10 MINUTE, NOW() - INTERVAL 8 MINUTE, '20대', 'F', 0),
(48, 4, NOW() - INTERVAL 8 MINUTE, NOW() - INTERVAL 6 MINUTE, '60대이상', 'F', 1),
(49, 5, NOW() - INTERVAL 6 MINUTE, NOW() - INTERVAL 3 MINUTE, '30대', 'M', 0),
(50, 1, NOW() - INTERVAL 3 MINUTE, NOW() - INTERVAL 1 MINUTE, '20대', 'F', 0);

-- ============================================
-- 9. 주문 (Orders) - 50개
-- ============================================
INSERT INTO orders (order_id, session_id, store_id, order_no, status, total_amount, created_at, paid_at, payment_method, payment_status, pg_transaction_id) VALUES
(1, 1, 1, 10001, 1, 4500, NOW() - INTERVAL 239 MINUTE, NOW() - INTERVAL 238 MINUTE, 'CARD', 'PAID', 'PG-20251210-00001'),
(2, 2, 1, 10002, 1, 9000, NOW() - INTERVAL 234 MINUTE, NOW() - INTERVAL 233 MINUTE, 'CARD', 'PAID', 'PG-20251210-00002'),
(3, 3, 2, 20001, 1, 5500, NOW() - INTERVAL 229 MINUTE, NOW() - INTERVAL 228 MINUTE, 'CARD', 'PAID', 'PG-20251210-00003'),
(4, 4, 1, 10003, 1, 9500, NOW() - INTERVAL 224 MINUTE, NOW() - INTERVAL 223 MINUTE, 'CASH', 'PAID', 'PG-20251210-00004'),
(5, 5, 2, 20002, 1, 10500, NOW() - INTERVAL 219 MINUTE, NOW() - INTERVAL 218 MINUTE, 'CARD', 'PAID', 'PG-20251210-00005'),
(6, 6, 3, 30001, 1, 9500, NOW() - INTERVAL 214 MINUTE, NOW() - INTERVAL 213 MINUTE, 'CARD', 'PAID', 'PG-20251210-00006'),
(7, 7, 1, 10004, 1, 5000, NOW() - INTERVAL 209 MINUTE, NOW() - INTERVAL 208 MINUTE, 'CARD', 'PAID', 'PG-20251210-00007'),
(8, 8, 2, 20003, 1, 12000, NOW() - INTERVAL 204 MINUTE, NOW() - INTERVAL 203 MINUTE, 'CARD', 'PAID', 'PG-20251210-00008'),
(9, 9, 3, 30002, 1, 8000, NOW() - INTERVAL 199 MINUTE, NOW() - INTERVAL 198 MINUTE, 'CASH', 'PAID', 'PG-20251210-00009'),
(10, 10, 4, 40001, 1, 14000, NOW() - INTERVAL 194 MINUTE, NOW() - INTERVAL 193 MINUTE, 'CARD', 'PAID', 'PG-20251210-00010'),
(11, 11, 2, 20004, 1, 9000, NOW() - INTERVAL 189 MINUTE, NOW() - INTERVAL 188 MINUTE, 'CARD', 'PAID', 'PG-20251210-00011'),
(12, 12, 3, 30003, 1, 8500, NOW() - INTERVAL 184 MINUTE, NOW() - INTERVAL 183 MINUTE, 'CARD', 'PAID', 'PG-20251210-00012'),
(13, 13, 4, 40002, 1, 11000, NOW() - INTERVAL 179 MINUTE, NOW() - INTERVAL 178 MINUTE, 'CARD', 'PAID', 'PG-20251210-00013'),
(14, 14, 5, 50001, 1, 10000, NOW() - INTERVAL 174 MINUTE, NOW() - INTERVAL 173 MINUTE, 'CASH', 'PAID', 'PG-20251210-00014'),
(15, 15, 1, 10005, 1, 8000, NOW() - INTERVAL 169 MINUTE, NOW() - INTERVAL 168 MINUTE, 'CARD', 'PAID', 'PG-20251210-00015'),
(16, 16, 2, 20005, 1, 13700, NOW() - INTERVAL 164 MINUTE, NOW() - INTERVAL 163 MINUTE, 'CARD', 'PAID', 'PG-20251210-00016'),
(17, 17, 3, 30004, 1, 5000, NOW() - INTERVAL 159 MINUTE, NOW() - INTERVAL 158 MINUTE, 'CARD', 'PAID', 'PG-20251210-00017'),
(18, 18, 4, 40003, 1, 13000, NOW() - INTERVAL 154 MINUTE, NOW() - INTERVAL 153 MINUTE, 'CARD', 'PAID', 'PG-20251210-00018'),
(19, 19, 5, 50002, 1, 4000, NOW() - INTERVAL 149 MINUTE, NOW() - INTERVAL 148 MINUTE, 'CARD', 'PAID', 'PG-20251210-00019'),
(20, 20, 1, 10006, 1, 16000, NOW() - INTERVAL 144 MINUTE, NOW() - INTERVAL 143 MINUTE, 'CASH', 'PAID', 'PG-20251210-00020'),
(21, 21, 2, 20006, 1, 11000, NOW() - INTERVAL 139 MINUTE, NOW() - INTERVAL 138 MINUTE, 'CARD', 'PAID', 'PG-20251210-00021'),
(22, 22, 3, 30005, 1, 8300, NOW() - INTERVAL 134 MINUTE, NOW() - INTERVAL 133 MINUTE, 'CARD', 'PAID', 'PG-20251210-00022'),
(23, 23, 4, 40004, 1, 12500, NOW() - INTERVAL 129 MINUTE, NOW() - INTERVAL 128 MINUTE, 'CARD', 'PAID', 'PG-20251210-00023'),
(24, 24, 5, 50003, 1, 11000, NOW() - INTERVAL 124 MINUTE, NOW() - INTERVAL 123 MINUTE, 'CARD', 'PAID', 'PG-20251210-00024'),
(25, 25, 1, 10007, 1, 13000, NOW() - INTERVAL 119 MINUTE, NOW() - INTERVAL 118 MINUTE, 'CARD', 'PAID', 'PG-20251210-00025'),
(26, 26, 2, 20007, 1, 9500, NOW() - INTERVAL 114 MINUTE, NOW() - INTERVAL 113 MINUTE, 'CASH', 'PAID', 'PG-20251210-00026'),
(27, 27, 3, 30006, 1, 8000, NOW() - INTERVAL 109 MINUTE, NOW() - INTERVAL 108 MINUTE, 'CARD', 'PAID', 'PG-20251210-00027'),
(28, 28, 4, 40005, 1, 12500, NOW() - INTERVAL 104 MINUTE, NOW() - INTERVAL 103 MINUTE, 'CARD', 'PAID', 'PG-20251210-00028'),
(29, 29, 5, 50004, 1, 8700, NOW() - INTERVAL 99 MINUTE, NOW() - INTERVAL 98 MINUTE, 'CARD', 'PAID', 'PG-20251210-00029'),
(30, 30, 1, 10008, 1, 10500, NOW() - INTERVAL 94 MINUTE, NOW() - INTERVAL 93 MINUTE, 'CARD', 'PAID', 'PG-20251210-00030'),
(31, 31, 2, 20008, 1, 8000, NOW() - INTERVAL 89 MINUTE, NOW() - INTERVAL 88 MINUTE, 'CARD', 'PAID', 'PG-20251210-00031'),
(32, 32, 3, 30007, 1, 12000, NOW() - INTERVAL 84 MINUTE, NOW() - INTERVAL 83 MINUTE, 'CASH', 'PAID', 'PG-20251210-00032'),
(33, 33, 4, 40006, 1, 9000, NOW() - INTERVAL 79 MINUTE, NOW() - INTERVAL 78 MINUTE, 'CARD', 'PAID', 'PG-20251210-00033'),
(34, 34, 5, 50005, 1, 8200, NOW() - INTERVAL 74 MINUTE, NOW() - INTERVAL 73 MINUTE, 'CARD', 'PAID', 'PG-20251210-00034'),
(35, 35, 1, 10009, 1, 13200, NOW() - INTERVAL 69 MINUTE, NOW() - INTERVAL 68 MINUTE, 'CARD', 'PAID', 'PG-20251210-00035'),
(36, 36, 2, 20009, 1, 5000, NOW() - INTERVAL 64 MINUTE, NOW() - INTERVAL 63 MINUTE, 'CARD', 'PAID', 'PG-20251210-00036'),
(37, 37, 3, 30008, 1, 10000, NOW() - INTERVAL 59 MINUTE, NOW() - INTERVAL 58 MINUTE, 'CARD', 'PAID', 'PG-20251210-00037'),
(38, 38, 4, 40007, 1, 13000, NOW() - INTERVAL 54 MINUTE, NOW() - INTERVAL 53 MINUTE, 'CASH', 'PAID', 'PG-20251210-00038'),
(39, 39, 5, 50006, 1, 9500, NOW() - INTERVAL 49 MINUTE, NOW() - INTERVAL 48 MINUTE, 'CARD', 'PAID', 'PG-20251210-00039'),
(40, 40, 1, 10010, 1, 10000, NOW() - INTERVAL 44 MINUTE, NOW() - INTERVAL 43 MINUTE, 'CARD', 'PAID', 'PG-20251210-00040'),
(41, 41, 2, 20010, 1, 8000, NOW() - INTERVAL 39 MINUTE, NOW() - INTERVAL 38 MINUTE, 'CARD', 'PAID', 'PG-20251210-00041'),
(42, 42, 3, 30009, 1, 11000, NOW() - INTERVAL 34 MINUTE, NOW() - INTERVAL 33 MINUTE, 'CARD', 'PAID', 'PG-20251210-00042'),
(43, 43, 4, 40008, 1, 7000, NOW() - INTERVAL 29 MINUTE, NOW() - INTERVAL 28 MINUTE, 'CASH', 'PAID', 'PG-20251210-00043'),
(44, 44, 5, 50007, 1, 11000, NOW() - INTERVAL 24 MINUTE, NOW() - INTERVAL 23 MINUTE, 'CARD', 'PAID', 'PG-20251210-00044'),
(45, 45, 1, 10011, 1, 12500, NOW() - INTERVAL 19 MINUTE, NOW() - INTERVAL 18 MINUTE, 'CARD', 'PAID', 'PG-20251210-00045'),
(46, 46, 2, 20011, 1, 8500, NOW() - INTERVAL 14 MINUTE, NOW() - INTERVAL 13 MINUTE, 'CARD', 'PAID', 'PG-20251210-00046'),
(47, 47, 3, 30010, 1, 9800, NOW() - INTERVAL 9 MINUTE, NOW() - INTERVAL 8 MINUTE, 'CARD', 'PAID', 'PG-20251210-00047'),
(48, 48, 4, 40009, 1, 8500, NOW() - INTERVAL 7 MINUTE, NOW() - INTERVAL 6 MINUTE, 'CASH', 'PAID', 'PG-20251210-00048'),
(49, 49, 5, 50008, 1, 12200, NOW() - INTERVAL 5 MINUTE, NOW() - INTERVAL 4 MINUTE, 'CARD', 'PAID', 'PG-20251210-00049'),
(50, 50, 1, 10012, 1, 10000, NOW() - INTERVAL 2 MINUTE, NOW() - INTERVAL 1 MINUTE, 'CARD', 'PAID', 'PG-20251210-00050');

-- ============================================
-- 10. 주문 아이템 (Order Item) - 다양한 메뉴 조합
-- ============================================
INSERT INTO order_item (order_item_id, order_id, menu_id, quantity, unit_price, line_amount) VALUES
-- 주문 1: 아메리카노 Large
(1, 1, 1, 1, 4000, 4500),
-- 주문 2: 카페라떼 x2
(2, 2, 2, 2, 4500, 9000),
-- 주문 3: 치즈케이크
(3, 3, 28, 1, 5500, 5500),
-- 주문 4: 아메리카노 + 치즈케이크
(4, 4, 1, 1, 4000, 4000),
(5, 4, 28, 1, 5500, 5500),
-- 주문 5: 카페라떼 + 티라미수
(6, 5, 2, 1, 4500, 4500),
(7, 5, 29, 1, 6000, 6000),
-- 주문 6: 레몬에이드 + 초코 브라우니
(8, 6, 16, 1, 5000, 5000),
(9, 6, 30, 1, 4500, 4500),
-- 주문 7: 카푸치노 Regular
(10, 7, 3, 1, 4500, 5000),
-- 주문 8: 카라멜 마키아또 + 마카롱 세트
(11, 8, 4, 1, 5000, 5000),
(12, 8, 31, 1, 7000, 7000),
-- 주문 9: 아메리카노 x2
(13, 9, 1, 2, 4000, 8000),
-- 주문 10: 바닐라 라떼 + 딸기라떼 + 휘낭시에
(14, 10, 5, 1, 5000, 5000),
(15, 10, 19, 1, 5500, 5500),
(16, 10, 32, 1, 3500, 3500),
-- 주문 11: 카페모카 + 스콘
(17, 11, 6, 1, 5000, 5000),
(18, 11, 36, 1, 4000, 4000),
-- 주문 12: 에스프레소 + 쿠키 세트
(19, 12, 7, 1, 3500, 3500),
(20, 12, 34, 1, 5000, 5000),
-- 주문 13: 크로와상 샌드위치 + 카페라떼
(21, 13, 38, 1, 6500, 6500),
(22, 13, 2, 1, 4500, 4500),
-- 주문 14: 콜드브루 + 치즈케이크
(23, 14, 8, 1, 4500, 4500),
(24, 14, 28, 1, 5500, 5500),
-- 주문 15: 아메리카노 x2
(25, 15, 1, 2, 4000, 8000),
-- 주문 16: 자몽에이드 + 레몬에이드 + 마들렌
(26, 16, 17, 1, 5200, 5200),
(27, 16, 16, 1, 5000, 5000),
(28, 16, 33, 1, 3500, 3500),
-- 주문 17: 녹차라떼 Regular
(29, 17, 21, 1, 5000, 5000),
-- 주문 18: 팬케이크 + 카페라떼
(30, 18, 41, 1, 7500, 9000),
(31, 18, 2, 1, 4500, 4500),
-- 주문 19: 아메리카노 Regular
(32, 19, 1, 1, 4000, 4000),
-- 주문 20: 카페라떼 + 치즈케이크 + 티라미수
(33, 20, 2, 1, 4500, 4500),
(34, 20, 28, 1, 5500, 5500),
(35, 20, 29, 1, 6000, 6000),
-- 주문 21: 딸기라떼 + 치즈케이크
(36, 21, 19, 1, 5500, 5500),
(37, 21, 28, 1, 5500, 5500),
-- 주문 22: 플랫화이트 + 소금빵
(38, 22, 9, 1, 4800, 4800),
(39, 22, 48, 1, 3500, 3500),
-- 주문 23: 아인슈페너 + 마카롱 세트
(40, 23, 10, 1, 5500, 5500),
(41, 23, 31, 1, 7000, 7000),
-- 주문 24: 베이글 샌드위치 + 초코라떼
(42, 24, 39, 1, 6000, 6000),
(43, 24, 20, 1, 5000, 5000),
-- 주문 25: 에그 베네딕트 + 카페라떼
(44, 25, 40, 1, 8500, 8500),
(45, 25, 2, 1, 4500, 4500),
-- 주문 26: 치즈케이크 + 아메리카노
(46, 26, 28, 1, 5500, 5500),
(47, 26, 1, 1, 4000, 4000),
-- 주문 27: 아메리카노 x2
(48, 27, 1, 2, 4000, 8000),
-- 주문 28: 와플 + 딸기라떼
(49, 28, 42, 1, 7000, 7000),
(50, 28, 19, 1, 5500, 5500),
-- 주문 29: 헤이즐넛 라떼 + 크로와상
(51, 29, 11, 1, 5200, 5200),
(52, 29, 46, 1, 3500, 3500),
-- 주문 30: 카페라떼 + 티라미수
(53, 30, 2, 1, 4500, 4500),
(54, 30, 29, 1, 6000, 6000),
-- 주문 31: 아메리카노 x2
(55, 31, 1, 2, 4000, 8000),
-- 주문 32: 프렌치토스트 + 카페라떼
(56, 32, 43, 1, 7500, 7500),
(57, 32, 2, 1, 4500, 4500),
-- 주문 33: 카라멜 마키아또 + 푸딩
(58, 33, 4, 1, 5000, 5000),
(59, 33, 35, 1, 4000, 4000),
-- 주문 34: 연유 라떼 + 베이글
(60, 34, 12, 1, 5200, 5200),
(61, 34, 47, 1, 3000, 3000),
-- 주문 35: 샐러드 + 자몽에이드
(62, 35, 44, 1, 8000, 8000),
(63, 35, 17, 1, 5200, 5200),
-- 주문 36: 레몬에이드 Regular
(64, 36, 16, 1, 5000, 5000),
-- 주문 37: 카페라떼 + 치즈케이크
(65, 37, 2, 1, 4500, 4500),
(66, 37, 28, 1, 5500, 5500),
-- 주문 38: 아보카도 토스트 + 카페라떼
(67, 38, 45, 1, 8500, 8500),
(68, 38, 2, 1, 4500, 4500),
-- 주문 39: 초코라떼 + 초코 브라우니
(69, 39, 20, 1, 5000, 5000),
(70, 39, 30, 1, 4500, 4500),
-- 주문 40: 카페라떼 + 치즈케이크
(71, 40, 2, 1, 4500, 4500),
(72, 40, 28, 1, 5500, 5500),
-- 주문 41: 아메리카노 x2
(73, 41, 1, 2, 4000, 8000),
-- 주문 42: 디카페인 라떼 + 티라미수
(74, 42, 14, 1, 5000, 5000),
(75, 42, 29, 1, 6000, 6000),
-- 주문 43: 유자차 + 모닝빵
(76, 43, 24, 1, 4500, 4500),
(77, 43, 49, 1, 2500, 2500),
-- 주문 44: 흑당라떼 + 치즈케이크
(78, 44, 22, 1, 5500, 5500),
(79, 44, 28, 1, 5500, 5500),
-- 주문 45: 더치커피 + 마카롱 세트
(80, 45, 15, 1, 5500, 5500),
(81, 45, 31, 1, 7000, 7000),
-- 주문 46: 카페모카 + 소금빵
(82, 46, 6, 1, 5000, 5000),
(83, 46, 48, 1, 3500, 3500),
-- 주문 47: 밀크티 + 쿠키 세트
(84, 47, 23, 1, 4800, 4800),
(85, 47, 34, 1, 5000, 5000),
-- 주문 48: 카페라떼 + 스콘
(86, 48, 2, 1, 4500, 4500),
(87, 48, 36, 1, 4000, 4000),
-- 주문 49: 청포도에이드 + 마카롱 세트
(88, 49, 18, 1, 5200, 5200),
(89, 49, 31, 1, 7000, 7000),
-- 주문 50: 카페라떼 + 치즈케이크
(90, 50, 2, 1, 4500, 4500),
(91, 50, 28, 1, 5500, 5500);

-- ============================================
-- 11. 주문 옵션 (Order Item Option)
-- ============================================
INSERT INTO order_item_option (order_item_option_id, order_item_id, option_item_id, extra_price) VALUES
-- 주문 1: 아메리카노 Large
(1, 1, 2, 500),
-- 주문 7: 카푸치노 샷추가
(2, 10, 6, 500),
(3, 10, 25, 500),
-- 주문 18: 팬케이크 토핑
(4, 30, 41, 1000),
(5, 30, 42, 1500),
-- 주문 24: 베이글 샌드위치
(6, 42, 39, 500);

-- ============================================
-- 12. 광고 컨텐츠 (Ad Content) - 10개
-- ============================================
INSERT INTO ad_content (ad_id, title, media_type, media_url, start_date, end_date, is_active) VALUES
(1, '아침 타임 아메리카노 1+1', 'IMAGE', '/ads/morning_americano_1plus1.png', '2025-12-01', '2025-12-31', 1),
(2, '점심 세트 20% 할인', 'IMAGE', '/ads/lunch_set_discount.png', '2025-12-01', '2025-12-31', 1),
(3, '시니어 전용 할인', 'IMAGE', '/ads/senior_discount.png', '2025-12-01', '2025-12-31', 1),
(4, '디저트 2+1 프로모션', 'IMAGE', '/ads/dessert_2plus1.png', '2025-12-01', '2025-12-15', 1),
(5, '신메뉴 출시 - 흑당라떼', 'VIDEO', '/ads/new_menu_brown_sugar.mp4', '2025-12-01', '2025-12-31', 1),
(6, '크리스마스 특별 세트', 'IMAGE', '/ads/christmas_special.png', '2025-12-10', '2025-12-25', 1),
(7, '여성 고객 베이커리 할인', 'IMAGE', '/ads/bakery_women_discount.png', '2025-12-01', '2025-12-31', 1),
(8, '브런치 메뉴 출시', 'VIDEO', '/ads/brunch_launch.mp4', '2025-12-01', '2025-12-31', 1),
(9, '학생 할인 이벤트', 'IMAGE', '/ads/student_discount.png', '2025-12-01', '2025-12-31', 1),
(10, '주말 특별 프로모션', 'IMAGE', '/ads/weekend_special.png', '2025-12-01', '2025-12-31', 1);

-- ============================================
-- 13. 광고 타겟 룰 (Ad Target Rule)
-- ============================================
INSERT INTO ad_target_rule (rule_id, ad_id, age_group, gender) VALUES
-- 광고 1: 아침 타임 아메리카노 - 모든 연령
(1, 1, '20대', NULL),
(2, 1, '30대', NULL),
(3, 1, '40대', NULL),
(4, 1, '50대', NULL),
-- 광고 2: 점심 세트 - 직장인 (30-40대)
(5, 2, '30대', NULL),
(6, 2, '40대', NULL),
-- 광고 3: 시니어 전용
(7, 3, '60대이상', NULL),
-- 광고 4: 디저트 - 여성 고객
(8, 4, '20대', 'F'),
(9, 4, '30대', 'F'),
-- 광고 5: 신메뉴 - 젊은층
(10, 5, '20대', NULL),
(11, 5, '30대', NULL),
-- 광고 6: 크리스마스 - 모든 고객
(12, 6, '20대', NULL),
(13, 6, '30대', NULL),
(14, 6, '40대', NULL),
-- 광고 7: 베이커리 - 여성
(15, 7, '20대', 'F'),
(16, 7, '30대', 'F'),
(17, 7, '40대', 'F'),
-- 광고 8: 브런치 - 30-40대
(18, 8, '30대', NULL),
(19, 8, '40대', NULL),
-- 광고 9: 학생 할인 - 20대
(20, 9, '20대', NULL),
-- 광고 10: 주말 프로모션 - 전체
(21, 10, NULL, NULL);

-- ============================================
-- 14. 광고 로그 (Ad Display Log) - 각 세션마다
-- ============================================
INSERT INTO ad_display_log (display_id, session_id, ad_id, displayed_at, duration_ms) VALUES
(1, 1, 9, NOW() - INTERVAL 239 MINUTE, 8000),
(2, 2, 2, NOW() - INTERVAL 234 MINUTE, 10000),
(3, 3, 4, NOW() - INTERVAL 229 MINUTE, 7500),
(4, 4, 3, NOW() - INTERVAL 224 MINUTE, 12000),
(5, 5, 5, NOW() - INTERVAL 219 MINUTE, 15000),
(6, 6, 4, NOW() - INTERVAL 214 MINUTE, 8500),
(7, 7, 2, NOW() - INTERVAL 209 MINUTE, 9000),
(8, 8, 4, NOW() - INTERVAL 204 MINUTE, 7000),
(9, 9, 3, NOW() - INTERVAL 199 MINUTE, 11000),
(10, 10, 2, NOW() - INTERVAL 194 MINUTE, 9500),
(11, 11, 8, NOW() - INTERVAL 189 MINUTE, 20000),
(12, 12, 5, NOW() - INTERVAL 184 MINUTE, 12000),
(13, 13, 4, NOW() - INTERVAL 179 MINUTE, 8000),
(14, 14, 8, NOW() - INTERVAL 174 MINUTE, 18000),
(15, 15, 3, NOW() - INTERVAL 169 MINUTE, 10000),
(16, 16, 4, NOW() - INTERVAL 164 MINUTE, 7500),
(17, 17, 5, NOW() - INTERVAL 159 MINUTE, 13000),
(18, 18, 8, NOW() - INTERVAL 154 MINUTE, 19000),
(19, 19, 9, NOW() - INTERVAL 149 MINUTE, 8000),
(20, 20, 3, NOW() - INTERVAL 144 MINUTE, 11500),
(21, 21, 4, NOW() - INTERVAL 139 MINUTE, 8200),
(22, 22, 2, NOW() - INTERVAL 134 MINUTE, 9300),
(23, 23, 9, NOW() - INTERVAL 129 MINUTE, 7800),
(24, 24, 8, NOW() - INTERVAL 124 MINUTE, 17000),
(25, 25, 8, NOW() - INTERVAL 119 MINUTE, 16500),
(26, 26, 5, NOW() - INTERVAL 114 MINUTE, 12500),
(27, 27, 3, NOW() - INTERVAL 109 MINUTE, 10500),
(28, 28, 4, NOW() - INTERVAL 104 MINUTE, 8300),
(29, 29, 2, NOW() - INTERVAL 99 MINUTE, 9200),
(30, 30, 4, NOW() - INTERVAL 94 MINUTE, 7900),
(31, 31, 5, NOW() - INTERVAL 89 MINUTE, 13500),
(32, 32, 8, NOW() - INTERVAL 84 MINUTE, 18500),
(33, 33, 9, NOW() - INTERVAL 79 MINUTE, 8100),
(34, 34, 3, NOW() - INTERVAL 74 MINUTE, 11200),
(35, 35, 2, NOW() - INTERVAL 69 MINUTE, 9400),
(36, 36, 2, NOW() - INTERVAL 64 MINUTE, 9100),
(37, 37, 5, NOW() - INTERVAL 59 MINUTE, 12800),
(38, 38, 4, NOW() - INTERVAL 54 MINUTE, 8400),
(39, 39, 8, NOW() - INTERVAL 49 MINUTE, 17500),
(40, 40, 4, NOW() - INTERVAL 44 MINUTE, 7700),
(41, 41, 3, NOW() - INTERVAL 39 MINUTE, 10800),
(42, 42, 5, NOW() - INTERVAL 34 MINUTE, 13200),
(43, 43, 2, NOW() - INTERVAL 29 MINUTE, 9600),
(44, 44, 9, NOW() - INTERVAL 24 MINUTE, 8200),
(45, 45, 4, NOW() - INTERVAL 19 MINUTE, 8100),
(46, 46, 2, NOW() - INTERVAL 14 MINUTE, 9300),
(47, 47, 9, NOW() - INTERVAL 9 MINUTE, 7900),
(48, 48, 3, NOW() - INTERVAL 7 MINUTE, 11000),
(49, 49, 5, NOW() - INTERVAL 5 MINUTE, 12600),
(50, 50, 4, NOW() - INTERVAL 2 MINUTE, 8000);

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 완료 메시지
-- ============================================
SELECT '데이터 초기화 및 입력 완료!' as message;
SELECT COUNT(*) as category_count FROM category;
SELECT COUNT(*) as store_count FROM store;
SELECT COUNT(*) as kiosk_count FROM kiosk;
SELECT COUNT(*) as admin_count FROM admin_user;
SELECT COUNT(*) as menu_count FROM menu_item;
SELECT COUNT(*) as option_group_count FROM option_group;
SELECT COUNT(*) as option_item_count FROM option_item;
SELECT COUNT(*) as session_count FROM customer_session;
SELECT COUNT(*) as order_count FROM orders;
SELECT COUNT(*) as order_item_count FROM order_item;
SELECT COUNT(*) as order_option_count FROM order_item_option;
SELECT COUNT(*) as ad_count FROM ad_content;
SELECT COUNT(*) as ad_rule_count FROM ad_target_rule;
SELECT COUNT(*) as ad_log_count FROM ad_display_log;
