-- ============================================
-- 개선된 키오스크 데이터베이스 DDL
-- 불필요한 복합키 제거 및 외래키 참조 수정
-- ============================================

-- 1. 카테고리 테이블
CREATE TABLE category (
    category_id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '커피/논커피/디저트',
    display_order INT NOT NULL COMMENT '정렬 순서 번호',
    PRIMARY KEY (category_id)
);

-- 2. 매장 테이블
CREATE TABLE store (
    store_id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '지점명',
    code VARCHAR(30) NOT NULL COMMENT 'GN01, HD01 지점 코드',
    address VARCHAR(255) NULL COMMENT '매장주소',
    is_active BOOLEAN NOT NULL COMMENT '활성화 여부',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (store_id),
    UNIQUE KEY uk_store_code (code)
);

-- 3. 키오스크 테이블
CREATE TABLE kiosk (
    kiosk_id BIGINT NOT NULL AUTO_INCREMENT,
    store_id BIGINT NOT NULL,
    kiosk_code VARCHAR(30) NOT NULL COMMENT 'GN01-K01 같은 코드(선택)',
    password VARCHAR(255) NOT NULL,
    location VARCHAR(50) NULL COMMENT '매장 내 위치(입구, 창가 등)',
    is_active BOOLEAN NOT NULL COMMENT '활성화 여부',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (kiosk_id),
    FOREIGN KEY (store_id) REFERENCES store(store_id)
);

-- 4. 관리자 테이블
CREATE TABLE admin_user (
    admin_id BIGINT NOT NULL AUTO_INCREMENT,
    store_id BIGINT NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL COMMENT '암호화',
    is_active BOOLEAN NOT NULL COMMENT '지점에 따른 활성화 여부',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (admin_id),
    FOREIGN KEY (store_id) REFERENCES store(store_id),
    UNIQUE KEY uk_username (username)
);

-- 5. 메뉴 아이템 테이블
CREATE TABLE menu_item (
    menu_id BIGINT NOT NULL AUTO_INCREMENT,
    category_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL COMMENT '메뉴 이름',
    price INT NOT NULL COMMENT '메뉴 가격',
    is_active BOOLEAN NOT NULL COMMENT '판매 여부',
    image_url VARCHAR(255) NULL COMMENT '이미지 경로',
    PRIMARY KEY (menu_id),
    FOREIGN KEY (category_id) REFERENCES category(category_id)
);

-- 6. 옵션 그룹 테이블
CREATE TABLE option_group (
    option_group_id BIGINT NOT NULL AUTO_INCREMENT,
    menu_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL COMMENT '그룹 이름',
    is_required BOOLEAN NOT NULL COMMENT '필수/비필수',
    selection_type VARCHAR(20) NOT NULL COMMENT '단수/복수 선택',
    PRIMARY KEY (option_group_id),
    FOREIGN KEY (menu_id) REFERENCES menu_item(menu_id)
);

-- 7. 옵션 아이템 테이블
CREATE TABLE option_item (
    option_item_id BIGINT NOT NULL AUTO_INCREMENT,
    option_group_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL COMMENT '옵션 이름',
    option_price INT NOT NULL COMMENT '옵션 가격',
    PRIMARY KEY (option_item_id),
    FOREIGN KEY (option_group_id) REFERENCES option_group(option_group_id)
);

-- 8. 고객 세션 테이블
CREATE TABLE customer_session (
    session_id BIGINT NOT NULL AUTO_INCREMENT,
    store_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '주문 시작 시간',
    ended_at TIMESTAMP NULL COMMENT '주문 완료 시간',
    age_group VARCHAR(50) NULL COMMENT '10대 20대 30대',
    gender ENUM('M','F') NULL COMMENT '남성/여성',
    is_senior_mode BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'FALSE: 일반/TRUE: 시니어 모드',
    PRIMARY KEY (session_id),
    FOREIGN KEY (store_id) REFERENCES store(store_id)
);

-- 9. 주문 테이블
CREATE TABLE orders (
    order_id BIGINT NOT NULL AUTO_INCREMENT,
    session_id BIGINT NOT NULL,
    store_id BIGINT NOT NULL,
    order_no INT NOT NULL COMMENT '주문번호',
    status TINYINT(1) NOT NULL COMMENT '주문 완료/취소',
    total_amount INT NOT NULL COMMENT '총 가격',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성시간',
    paid_at TIMESTAMP NULL COMMENT '결제 시간',
    payment_method VARCHAR(20) NULL COMMENT '카드/현금/Pay/쿠폰',
    payment_status VARCHAR(20) NULL COMMENT '결제 상태',
    pg_transaction_id VARCHAR(50) NULL COMMENT 'PG사가 준 비식별 ID',
    PRIMARY KEY (order_id),
    FOREIGN KEY (session_id) REFERENCES customer_session(session_id),
    FOREIGN KEY (store_id) REFERENCES store(store_id)
);

-- 10. 주문 아이템 테이블
CREATE TABLE order_item (
    order_item_id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    quantity INT NOT NULL COMMENT '수량',
    unit_price INT NOT NULL COMMENT '개별 금액',
    line_amount INT NOT NULL COMMENT '총 금액',
    PRIMARY KEY (order_item_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (menu_id) REFERENCES menu_item(menu_id)
);

-- 11. 주문 아이템 옵션 테이블
CREATE TABLE order_item_option (
    order_item_option_id BIGINT NOT NULL AUTO_INCREMENT,
    order_item_id BIGINT NOT NULL,
    option_item_id BIGINT NOT NULL,
    extra_price INT NOT NULL COMMENT '옵션 금액',
    PRIMARY KEY (order_item_option_id),
    FOREIGN KEY (order_item_id) REFERENCES order_item(order_item_id),
    FOREIGN KEY (option_item_id) REFERENCES option_item(option_item_id)
);

-- 12. 광고 컨텐츠 테이블
CREATE TABLE ad_content (
    ad_id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(50) NOT NULL COMMENT '광고 이름',
    media_type VARCHAR(20) NOT NULL COMMENT '이미지 / 비디오',
    media_url VARCHAR(255) NOT NULL COMMENT '광고 원본 경로',
    start_date DATE NULL COMMENT '광고 계약 시작 날짜',
    end_date DATE NULL COMMENT '광고 계약 종료 날짜',
    is_active BOOLEAN NOT NULL COMMENT '사용 여부',
    PRIMARY KEY (ad_id)
);

-- 13. 광고 타겟 룰 테이블
CREATE TABLE ad_target_rule (
    rule_id BIGINT NOT NULL AUTO_INCREMENT,
    ad_id BIGINT NOT NULL,
    age_group VARCHAR(20) NULL COMMENT '타겟 나이',
    gender ENUM('M', 'F') NULL COMMENT '타겟 성별 남성/여성',
    PRIMARY KEY (rule_id),
    FOREIGN KEY (ad_id) REFERENCES ad_content(ad_id)
);

-- 14. 광고 표시 로그 테이블
CREATE TABLE ad_display_log (
    display_id BIGINT NOT NULL AUTO_INCREMENT,
    session_id BIGINT NOT NULL,
    ad_id BIGINT NOT NULL,
    displayed_at DATETIME NOT NULL COMMENT '표시 시작 시간',
    duration_ms INT NULL COMMENT '노출 시간',
    PRIMARY KEY (display_id),
    FOREIGN KEY (session_id) REFERENCES customer_session(session_id),
    FOREIGN KEY (ad_id) REFERENCES ad_content(ad_id)
);

-- ============================================
-- 인덱스 추가 (성능 최적화)
-- ============================================

-- 주문 조회 최적화
CREATE INDEX idx_orders_store_created ON orders(store_id, created_at);
CREATE INDEX idx_orders_session ON orders(session_id);

-- 세션 조회 최적화
CREATE INDEX idx_session_store_created ON customer_session(store_id, created_at);

-- 주문 아이템 조회 최적화
CREATE INDEX idx_order_item_order ON order_item(order_id);
CREATE INDEX idx_order_item_menu ON order_item(menu_id);

-- 옵션 조회 최적화
CREATE INDEX idx_option_item_group ON option_item(option_group_id);
CREATE INDEX idx_option_group_menu ON option_group(menu_id);

-- 광고 로그 조회 최적화
CREATE INDEX idx_ad_log_session ON ad_display_log(session_id);
CREATE INDEX idx_ad_log_displayed ON ad_display_log(displayed_at);