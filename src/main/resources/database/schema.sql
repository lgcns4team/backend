-- ============================================
-- NokNok 키오스크 데이터베이스 스키마
-- ============================================
SET
    FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- DROP TABLES
-- ============================================
DROP TABLE IF EXISTS `ad_display_log`;

DROP TABLE IF EXISTS `ad_target_rule`;

DROP TABLE IF EXISTS `ad_content`;

DROP TABLE IF EXISTS `order_item_option`;

DROP TABLE IF EXISTS `order_item`;

DROP TABLE IF EXISTS `orders`;

DROP TABLE IF EXISTS `customer_session`;

DROP TABLE IF EXISTS `option_item`;

DROP TABLE IF EXISTS `option_group`;

DROP TABLE IF EXISTS `menu_item`;

DROP TABLE IF EXISTS `admin_user`;

DROP TABLE IF EXISTS `kiosk`;

DROP TABLE IF EXISTS `store`;

DROP TABLE IF EXISTS `category`;

-- ============================================
-- CREATE TABLES
-- ============================================
--
-- Table structure for table `category`
--
CREATE TABLE
    `category` (
        `category_id` bigint NOT NULL AUTO_INCREMENT,
        `name` varchar(50) NOT NULL COMMENT '커피/논커피/디저트',
        `display_order` int NOT NULL COMMENT '정렬 순서 번호',
        PRIMARY KEY (`category_id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

--
-- Table structure for table `store`
--
CREATE TABLE
    `store` (
        `store_id` bigint NOT NULL AUTO_INCREMENT,
        `name` varchar(50) NOT NULL COMMENT '지점명',
        `code` varchar(30) NOT NULL COMMENT 'GN01, HD01 지점 코드',
        `address` varchar(255) DEFAULT NULL COMMENT '매장주소',
        `is_active` tinyint (1) NOT NULL COMMENT '활성화 여부',
        `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (`store_id`),
        UNIQUE KEY `uk_store_code` (`code`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

--
-- Table structure for table `kiosk`
--
CREATE TABLE
    `kiosk` (
        `kiosk_id` bigint NOT NULL AUTO_INCREMENT,
        `store_id` bigint NOT NULL,
        `kiosk_code` varchar(30) NOT NULL COMMENT 'GN01-K01 같은 코드(선택)',
        `password` varchar(255) NOT NULL,
        `location` varchar(50) DEFAULT NULL COMMENT '매장 내 위치(입구, 창가 등)',
        `is_active` tinyint (1) NOT NULL COMMENT '활성화 여부',
        `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (`kiosk_id`),
        KEY `store_id` (`store_id`),
        CONSTRAINT `kiosk_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

--
-- Table structure for table `admin_user`
--
CREATE TABLE
    `admin_user` (
        `admin_id` bigint NOT NULL AUTO_INCREMENT,
        `store_id` bigint NOT NULL,
        `username` varchar(50) NOT NULL,
        `password` varchar(255) NOT NULL COMMENT '암호화',
        `is_active` tinyint (1) NOT NULL COMMENT '지점에 따른 활성화 여부',
        `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (`admin_id`),
        UNIQUE KEY `uk_username` (`username`),
        KEY `store_id` (`store_id`),
        CONSTRAINT `admin_user_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

--
-- Table structure for table `menu_item`
--
CREATE TABLE
    `menu_item` (
        `menu_id` bigint NOT NULL AUTO_INCREMENT,
        `category_id` bigint NOT NULL,
        `name` varchar(50) NOT NULL COMMENT '메뉴 이름',
        `price` int NOT NULL COMMENT '메뉴 가격',
        `is_active` tinyint (1) NOT NULL COMMENT '판매 여부',
        `image_url` varchar(255) DEFAULT NULL COMMENT '이미지 경로',
        PRIMARY KEY (`menu_id`),
        KEY `category_id` (`category_id`),
        CONSTRAINT `menu_item_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

--
-- Table structure for table `option_group`
--
CREATE TABLE
    `option_group` (
        `option_group_id` bigint NOT NULL AUTO_INCREMENT,
        `menu_id` bigint NOT NULL,
        `name` varchar(50) NOT NULL COMMENT '그룹 이름',
        `is_required` tinyint (1) NOT NULL COMMENT '필수/비필수',
        `selection_type` varchar(20) NOT NULL COMMENT '단수/복수 선택',
        PRIMARY KEY (`option_group_id`),
        KEY `menu_id` (`menu_id`),
        KEY `idx_option_group_menu` (`menu_id`),
        CONSTRAINT `option_group_ibfk_1` FOREIGN KEY (`menu_id`) REFERENCES `menu_item` (`menu_id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

--
-- Table structure for table `option_item`
--
CREATE TABLE
    `option_item` (
        `option_item_id` bigint NOT NULL AUTO_INCREMENT,
        `option_group_id` bigint NOT NULL,
        `name` varchar(50) NOT NULL COMMENT '옵션 이름',
        `option_price` int NOT NULL COMMENT '옵션 가격',
        PRIMARY KEY (`option_item_id`),
        KEY `option_group_id` (`option_group_id`),
        KEY `idx_option_item_group` (`option_group_id`),
        CONSTRAINT `option_item_ibfk_1` FOREIGN KEY (`option_group_id`) REFERENCES `option_group` (`option_group_id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

--
-- Table structure for table `customer_session`
--
CREATE TABLE
    `customer_session` (
        `session_id` bigint NOT NULL AUTO_INCREMENT,
        `store_id` bigint NOT NULL,
        `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '주문 시작 시간',
        `ended_at` timestamp NULL DEFAULT NULL COMMENT '주문 완료 시간',
        `age_group` varchar(50) DEFAULT NULL COMMENT '10대 20대 30대',
        `gender` enum ('M', 'F') DEFAULT NULL COMMENT '남성/여성',
        `is_senior_mode` tinyint (1) NOT NULL DEFAULT '0' COMMENT 'FALSE: 일반/TRUE: 시니어 모드',
        PRIMARY KEY (`session_id`),
        KEY `store_id` (`store_id`),
        KEY `idx_session_store_created` (`store_id`, `created_at`),
        CONSTRAINT `customer_session_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

--
-- Table structure for table `orders`
--
CREATE TABLE
    `orders` (
        `order_id` bigint NOT NULL AUTO_INCREMENT,
        `session_id` bigint NOT NULL,
        `store_id` bigint NOT NULL,
        `order_no` int NOT NULL COMMENT '주문번호',
        `order_type` enum ('dine-in', 'takeout') NOT NULL DEFAULT 'dine-in' COMMENT '매장식사/포장',
        `status` tinyint (1) NOT NULL COMMENT '주문 완료/취소',
        `total_amount` int NOT NULL COMMENT '총 가격',
        `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성시간',
        `paid_at` timestamp NULL DEFAULT NULL COMMENT '결제 시간',
        `payment_method` varchar(20) DEFAULT NULL COMMENT '카드결제/네이버페이/카카오페이/삼성페이/애플페이/기프티콘',
        `payment_status` varchar(20) DEFAULT NULL COMMENT '결제 상태',
        `pg_transaction_id` varchar(50) DEFAULT NULL COMMENT 'PG사가 준 비식별 ID',
        PRIMARY KEY (`order_id`),
        KEY `session_id` (`session_id`),
        KEY `store_id` (`store_id`),
        KEY `idx_orders_store_created` (`store_id`, `created_at`),
        KEY `idx_orders_session` (`session_id`),
        CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`session_id`) REFERENCES `customer_session` (`session_id`),
        CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

--
-- Table structure for table `order_item`
--
CREATE TABLE
    `order_item` (
        `order_item_id` bigint NOT NULL AUTO_INCREMENT,
        `order_id` bigint NOT NULL,
        `menu_id` bigint NOT NULL,
        `quantity` int NOT NULL COMMENT '수량',
        `unit_price` int NOT NULL COMMENT '개별 금액',
        `line_amount` int NOT NULL COMMENT '총 금액',
        PRIMARY KEY (`order_item_id`),
        KEY `order_id` (`order_id`),
        KEY `menu_id` (`menu_id`),
        KEY `idx_order_item_order` (`order_id`),
        KEY `idx_order_item_menu` (`menu_id`),
        CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
        CONSTRAINT `order_item_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `menu_item` (`menu_id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

--
-- Table structure for table `order_item_option`
--
CREATE TABLE
    `order_item_option` (
        `order_item_option_id` bigint NOT NULL AUTO_INCREMENT,
        `order_item_id` bigint NOT NULL,
        `option_item_id` bigint NOT NULL,
        `extra_price` int NOT NULL COMMENT '옵션 금액',
        `option_quantity` int NOT NULL COMMENT '옵션 수량',
        PRIMARY KEY (`order_item_option_id`),
        KEY `order_item_id` (`order_item_id`),
        KEY `option_item_id` (`option_item_id`),
        CONSTRAINT `order_item_option_ibfk_1` FOREIGN KEY (`order_item_id`) REFERENCES `order_item` (`order_item_id`),
        CONSTRAINT `order_item_option_ibfk_2` FOREIGN KEY (`option_item_id`) REFERENCES `option_item` (`option_item_id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

--
-- Table structure for table `ad_content`
--
CREATE TABLE
    `ad_content` (
        `ad_id` bigint NOT NULL AUTO_INCREMENT,
        `title` varchar(50) NOT NULL COMMENT '광고 이름',
        `media_type` varchar(20) NOT NULL COMMENT '이미지 / 비디오',
        `media_url` varchar(255) NOT NULL COMMENT '광고 원본 경로',
        `start_date` date DEFAULT NULL COMMENT '광고 계약 시작 날짜',
        `end_date` date DEFAULT NULL COMMENT '광고 계약 종료 날짜',
        `is_active` tinyint (1) NOT NULL COMMENT '사용 여부',
        PRIMARY KEY (`ad_id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

--
-- Table structure for table `ad_target_rule`
--
CREATE TABLE
    `ad_target_rule` (
        `rule_id` bigint NOT NULL AUTO_INCREMENT,
        `ad_id` bigint NOT NULL,
        `age_group` varchar(20) DEFAULT NULL COMMENT '타겟 나이',
        `gender` enum ('M', 'F') DEFAULT NULL COMMENT '타겟 성별 남성/여성',
        PRIMARY KEY (`rule_id`),
        KEY `ad_id` (`ad_id`),
        CONSTRAINT `ad_target_rule_ibfk_1` FOREIGN KEY (`ad_id`) REFERENCES `ad_content` (`ad_id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

--
-- Table structure for table `ad_display_log`
--
CREATE TABLE
    `ad_display_log` (
        `display_id` bigint NOT NULL AUTO_INCREMENT,
        `ad_id` bigint NOT NULL,
        `displayed_at` datetime NOT NULL COMMENT '표시 시작 시간',
        `duration_ms` int DEFAULT NULL COMMENT '노출 시간',
        PRIMARY KEY (`display_id`),
        KEY `ad_id` (`ad_id`),
        KEY `idx_ad_log_ad` (`ad_id`),
        KEY `idx_ad_log_displayed` (`displayed_at`),
        CONSTRAINT `ad_display_log_ibfk_1` FOREIGN KEY (`ad_id`) REFERENCES `ad_content` (`ad_id`)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

SET
    FOREIGN_KEY_CHECKS = 1;