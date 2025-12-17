-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        10.11.14-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  12.11.0.7065
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 테이블 nok_nok.admin_user 구조 내보내기
CREATE TABLE IF NOT EXISTS `admin_user` (
  `admin_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `store_id` bigint(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL COMMENT '암호화',
  `is_active` tinyint(1) NOT NULL COMMENT '지점에 따른 활성화 여부',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `store_id` (`store_id`),
  CONSTRAINT `admin_user_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- 테이블 데이터 nok_nok.admin_user:~7 rows (대략적) 내보내기
INSERT INTO `admin_user` (`admin_id`, `store_id`, `username`, `password`, `is_active`, `created_at`) VALUES
	(1, 1, 'gn_admin', 'admin1234', 1, '2025-12-17 07:53:17'),
	(2, 1, 'gn_manager', 'manager1234', 1, '2025-12-17 07:53:17'),
	(3, 2, 'hd_admin', 'admin1234', 1, '2025-12-17 07:53:17'),
	(4, 3, 'sc_admin', 'admin1234', 1, '2025-12-17 07:53:17'),
	(5, 4, 'js_admin', 'admin1234', 1, '2025-12-17 07:53:17'),
	(6, 4, 'js_manager', 'manager1234', 1, '2025-12-17 07:53:17'),
	(7, 5, 'md_admin', 'admin1234', 1, '2025-12-17 07:53:17');

-- 테이블 nok_nok.ad_content 구조 내보내기
CREATE TABLE IF NOT EXISTS `ad_content` (
  `ad_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) NOT NULL COMMENT '광고 이름',
  `media_type` varchar(20) NOT NULL COMMENT '이미지 / 비디오',
  `media_url` varchar(255) NOT NULL COMMENT '광고 원본 경로',
  `start_date` date DEFAULT NULL COMMENT '광고 계약 시작 날짜',
  `end_date` date DEFAULT NULL COMMENT '광고 계약 종료 날짜',
  `is_active` tinyint(1) NOT NULL COMMENT '사용 여부',
  PRIMARY KEY (`ad_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- 테이블 데이터 nok_nok.ad_content:~10 rows (대략적) 내보내기
INSERT INTO `ad_content` (`ad_id`, `title`, `media_type`, `media_url`, `start_date`, `end_date`, `is_active`) VALUES
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

-- 테이블 nok_nok.ad_display_log 구조 내보내기
CREATE TABLE IF NOT EXISTS `ad_display_log` (
  `display_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_id` bigint(20) NOT NULL,
  `ad_id` bigint(20) NOT NULL,
  `displayed_at` datetime NOT NULL COMMENT '표시 시작 시간',
  `duration_ms` int(11) DEFAULT NULL COMMENT '노출 시간',
  PRIMARY KEY (`display_id`),
  KEY `ad_id` (`ad_id`),
  KEY `idx_ad_log_session` (`session_id`),
  KEY `idx_ad_log_displayed` (`displayed_at`),
  CONSTRAINT `ad_display_log_ibfk_1` FOREIGN KEY (`session_id`) REFERENCES `customer_session` (`session_id`),
  CONSTRAINT `ad_display_log_ibfk_2` FOREIGN KEY (`ad_id`) REFERENCES `ad_content` (`ad_id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- 테이블 데이터 nok_nok.ad_display_log:~50 rows (대략적) 내보내기
INSERT INTO `ad_display_log` (`display_id`, `session_id`, `ad_id`, `displayed_at`, `duration_ms`) VALUES
	(1, 1, 9, '2025-12-17 12:54:17', 8000),
	(2, 2, 2, '2025-12-17 12:59:17', 10000),
	(3, 3, 4, '2025-12-17 13:04:17', 7500),
	(4, 4, 3, '2025-12-17 13:09:17', 12000),
	(5, 5, 5, '2025-12-17 13:14:17', 15000),
	(6, 6, 4, '2025-12-17 13:19:17', 8500),
	(7, 7, 2, '2025-12-17 13:24:17', 9000),
	(8, 8, 4, '2025-12-17 13:29:17', 7000),
	(9, 9, 3, '2025-12-17 13:34:17', 11000),
	(10, 10, 2, '2025-12-17 13:39:17', 9500),
	(11, 11, 8, '2025-12-17 13:44:17', 20000),
	(12, 12, 5, '2025-12-17 13:49:17', 12000),
	(13, 13, 4, '2025-12-17 13:54:17', 8000),
	(14, 14, 8, '2025-12-17 13:59:17', 18000),
	(15, 15, 3, '2025-12-17 14:04:17', 10000),
	(16, 16, 4, '2025-12-17 14:09:17', 7500),
	(17, 17, 5, '2025-12-17 14:14:17', 13000),
	(18, 18, 8, '2025-12-17 14:19:17', 19000),
	(19, 19, 9, '2025-12-17 14:24:17', 8000),
	(20, 20, 3, '2025-12-17 14:29:17', 11500),
	(21, 21, 4, '2025-12-17 14:34:17', 8200),
	(22, 22, 2, '2025-12-17 14:39:17', 9300),
	(23, 23, 9, '2025-12-17 14:44:17', 7800),
	(24, 24, 8, '2025-12-17 14:49:17', 17000),
	(25, 25, 8, '2025-12-17 14:54:17', 16500),
	(26, 26, 5, '2025-12-17 14:59:17', 12500),
	(27, 27, 3, '2025-12-17 15:04:17', 10500),
	(28, 28, 4, '2025-12-17 15:09:17', 8300),
	(29, 29, 2, '2025-12-17 15:14:17', 9200),
	(30, 30, 4, '2025-12-17 15:19:17', 7900),
	(31, 31, 5, '2025-12-17 15:24:17', 13500),
	(32, 32, 8, '2025-12-17 15:29:17', 18500),
	(33, 33, 9, '2025-12-17 15:34:17', 8100),
	(34, 34, 3, '2025-12-17 15:39:17', 11200),
	(35, 35, 2, '2025-12-17 15:44:17', 9400),
	(36, 36, 2, '2025-12-17 15:49:17', 9100),
	(37, 37, 5, '2025-12-17 15:54:17', 12800),
	(38, 38, 4, '2025-12-17 15:59:17', 8400),
	(39, 39, 8, '2025-12-17 16:04:17', 17500),
	(40, 40, 4, '2025-12-17 16:09:17', 7700),
	(41, 41, 3, '2025-12-17 16:14:17', 10800),
	(42, 42, 5, '2025-12-17 16:19:17', 13200),
	(43, 43, 2, '2025-12-17 16:24:17', 9600),
	(44, 44, 9, '2025-12-17 16:29:17', 8200),
	(45, 45, 4, '2025-12-17 16:34:17', 8100),
	(46, 46, 2, '2025-12-17 16:39:17', 9300),
	(47, 47, 9, '2025-12-17 16:44:17', 7900),
	(48, 48, 3, '2025-12-17 16:46:17', 11000),
	(49, 49, 5, '2025-12-17 16:48:17', 12600),
	(50, 50, 4, '2025-12-17 16:51:17', 8000);

-- 테이블 nok_nok.ad_target_rule 구조 내보내기
CREATE TABLE IF NOT EXISTS `ad_target_rule` (
  `rule_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ad_id` bigint(20) NOT NULL,
  `age_group` varchar(20) DEFAULT NULL COMMENT '타겟 나이',
  `gender` enum('M','F') DEFAULT NULL COMMENT '타겟 성별 남성/여성',
  PRIMARY KEY (`rule_id`),
  KEY `ad_id` (`ad_id`),
  CONSTRAINT `ad_target_rule_ibfk_1` FOREIGN KEY (`ad_id`) REFERENCES `ad_content` (`ad_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- 테이블 데이터 nok_nok.ad_target_rule:~21 rows (대략적) 내보내기
INSERT INTO `ad_target_rule` (`rule_id`, `ad_id`, `age_group`, `gender`) VALUES
	(1, 1, '20대', NULL),
	(2, 1, '30대', NULL),
	(3, 1, '40대', NULL),
	(4, 1, '50대', NULL),
	(5, 2, '30대', NULL),
	(6, 2, '40대', NULL),
	(7, 3, '60대이상', NULL),
	(8, 4, '20대', 'F'),
	(9, 4, '30대', 'F'),
	(10, 5, '20대', NULL),
	(11, 5, '30대', NULL),
	(12, 6, '20대', NULL),
	(13, 6, '30대', NULL),
	(14, 6, '40대', NULL),
	(15, 7, '20대', 'F'),
	(16, 7, '30대', 'F'),
	(17, 7, '40대', 'F'),
	(18, 8, '30대', NULL),
	(19, 8, '40대', NULL),
	(20, 9, '20대', NULL),
	(21, 10, NULL, NULL);

-- 테이블 nok_nok.category 구조 내보내기
CREATE TABLE IF NOT EXISTS `category` (
  `category_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '커피/논커피/디저트',
  `display_order` int(11) NOT NULL COMMENT '정렬 순서 번호',
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- 테이블 데이터 nok_nok.category:~5 rows (대략적) 내보내기
INSERT INTO `category` (`category_id`, `name`, `display_order`) VALUES
	(1, '커피', 1),
	(2, '음료', 2),
	(3, '디저트', 3),
	(4, '브런치', 4),
	(5, '베이커리', 5);

-- 테이블 nok_nok.customer_session 구조 내보내기
CREATE TABLE IF NOT EXISTS `customer_session` (
  `session_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `store_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() COMMENT '주문 시작 시간',
  `ended_at` timestamp NULL DEFAULT NULL COMMENT '주문 완료 시간',
  `age_group` varchar(50) DEFAULT NULL COMMENT '10대 20대 30대',
  `gender` enum('M','F') DEFAULT NULL COMMENT '남성/여성',
  `is_senior_mode` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'FALSE: 일반/TRUE: 시니어 모드',
  PRIMARY KEY (`session_id`),
  KEY `idx_session_store_created` (`store_id`,`created_at`),
  CONSTRAINT `customer_session_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- 테이블 데이터 nok_nok.customer_session:~50 rows (대략적) 내보내기
INSERT INTO `customer_session` (`session_id`, `store_id`, `created_at`, `ended_at`, `age_group`, `gender`, `is_senior_mode`) VALUES
	(1, 1, '2025-12-17 03:53:17', '2025-12-17 03:58:17', '20대', 'F', 0),
	(2, 1, '2025-12-17 03:58:17', '2025-12-17 04:03:17', '30대', 'M', 0),
	(3, 2, '2025-12-17 04:03:17', '2025-12-17 04:08:17', '40대', 'F', 0),
	(4, 1, '2025-12-17 04:08:17', '2025-12-17 04:13:17', '60대이상', 'M', 1),
	(5, 2, '2025-12-17 04:13:17', '2025-12-17 04:18:17', '20대', 'M', 0),
	(6, 3, '2025-12-17 04:18:17', '2025-12-17 04:23:17', '30대', 'F', 0),
	(7, 1, '2025-12-17 04:23:17', '2025-12-17 04:28:17', '50대', 'M', 0),
	(8, 2, '2025-12-17 04:28:17', '2025-12-17 04:33:17', '20대', 'F', 0),
	(9, 3, '2025-12-17 04:33:17', '2025-12-17 04:38:17', '60대이상', 'F', 1),
	(10, 4, '2025-12-17 04:38:17', '2025-12-17 04:43:17', '30대', 'M', 0),
	(11, 2, '2025-12-17 04:43:17', '2025-12-17 04:48:17', '40대', 'M', 0),
	(12, 3, '2025-12-17 04:48:17', '2025-12-17 04:53:17', '20대', 'M', 0),
	(13, 4, '2025-12-17 04:53:17', '2025-12-17 04:58:17', '30대', 'F', 0),
	(14, 5, '2025-12-17 04:58:17', '2025-12-17 05:03:17', '50대', 'F', 0),
	(15, 1, '2025-12-17 05:03:17', '2025-12-17 05:08:17', '60대이상', 'M', 1),
	(16, 2, '2025-12-17 05:08:17', '2025-12-17 05:13:17', '20대', 'F', 0),
	(17, 3, '2025-12-17 05:13:17', '2025-12-17 05:18:17', '30대', 'M', 0),
	(18, 4, '2025-12-17 05:18:17', '2025-12-17 05:23:17', '40대', 'F', 0),
	(19, 5, '2025-12-17 05:23:17', '2025-12-17 05:28:17', '20대', 'M', 0),
	(20, 1, '2025-12-17 05:28:17', '2025-12-17 05:33:17', '60대이상', 'F', 1),
	(21, 2, '2025-12-17 05:33:17', '2025-12-17 05:38:17', '30대', 'F', 0),
	(22, 3, '2025-12-17 05:38:17', '2025-12-17 05:43:17', '50대', 'M', 0),
	(23, 4, '2025-12-17 05:43:17', '2025-12-17 05:48:17', '20대', 'F', 0),
	(24, 5, '2025-12-17 05:48:17', '2025-12-17 05:53:17', '30대', 'M', 0),
	(25, 1, '2025-12-17 05:53:17', '2025-12-17 05:58:17', '40대', 'F', 0),
	(26, 2, '2025-12-17 05:58:17', '2025-12-17 06:03:17', '20대', 'M', 0),
	(27, 3, '2025-12-17 06:03:17', '2025-12-17 06:08:17', '60대이상', 'M', 1),
	(28, 4, '2025-12-17 06:08:17', '2025-12-17 06:13:17', '30대', 'F', 0),
	(29, 5, '2025-12-17 06:13:17', '2025-12-17 06:18:17', '50대', 'F', 0),
	(30, 1, '2025-12-17 06:18:17', '2025-12-17 06:23:17', '20대', 'F', 0),
	(31, 2, '2025-12-17 06:23:17', '2025-12-17 06:28:17', '30대', 'M', 0),
	(32, 3, '2025-12-17 06:28:17', '2025-12-17 06:33:17', '40대', 'M', 0),
	(33, 4, '2025-12-17 06:33:17', '2025-12-17 06:38:17', '20대', 'F', 0),
	(34, 5, '2025-12-17 06:38:17', '2025-12-17 06:43:17', '60대이상', 'F', 1),
	(35, 1, '2025-12-17 06:43:17', '2025-12-17 06:48:17', '30대', 'M', 0),
	(36, 2, '2025-12-17 06:48:17', '2025-12-17 06:53:17', '50대', 'M', 0),
	(37, 3, '2025-12-17 06:53:17', '2025-12-17 06:58:17', '20대', 'M', 0),
	(38, 4, '2025-12-17 06:58:17', '2025-12-17 07:03:17', '30대', 'F', 0),
	(39, 5, '2025-12-17 07:03:17', '2025-12-17 07:08:17', '40대', 'F', 0),
	(40, 1, '2025-12-17 07:08:17', '2025-12-17 07:13:17', '20대', 'F', 0),
	(41, 2, '2025-12-17 07:13:17', '2025-12-17 07:18:17', '60대이상', 'M', 1),
	(42, 3, '2025-12-17 07:18:17', '2025-12-17 07:23:17', '30대', 'M', 0),
	(43, 4, '2025-12-17 07:23:17', '2025-12-17 07:28:17', '50대', 'F', 0),
	(44, 5, '2025-12-17 07:28:17', '2025-12-17 07:33:17', '20대', 'M', 0),
	(45, 1, '2025-12-17 07:33:17', '2025-12-17 07:38:17', '30대', 'F', 0),
	(46, 2, '2025-12-17 07:38:17', '2025-12-17 07:43:17', '40대', 'M', 0),
	(47, 3, '2025-12-17 07:43:17', '2025-12-17 07:45:17', '20대', 'F', 0),
	(48, 4, '2025-12-17 07:45:17', '2025-12-17 07:47:17', '60대이상', 'F', 1),
	(49, 5, '2025-12-17 07:47:17', '2025-12-17 07:50:17', '30대', 'M', 0),
	(50, 1, '2025-12-17 07:50:17', '2025-12-17 07:52:17', '20대', 'F', 0);

-- 테이블 nok_nok.kiosk 구조 내보내기
CREATE TABLE IF NOT EXISTS `kiosk` (
  `kiosk_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `store_id` bigint(20) NOT NULL,
  `kiosk_code` varchar(30) NOT NULL COMMENT 'GN01-K01 같은 코드(선택)',
  `password` varchar(255) NOT NULL,
  `location` varchar(50) DEFAULT NULL COMMENT '매장 내 위치(입구, 창가 등)',
  `is_active` tinyint(1) NOT NULL COMMENT '활성화 여부',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`kiosk_id`),
  KEY `store_id` (`store_id`),
  CONSTRAINT `kiosk_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- 테이블 데이터 nok_nok.kiosk:~11 rows (대략적) 내보내기
INSERT INTO `kiosk` (`kiosk_id`, `store_id`, `kiosk_code`, `password`, `location`, `is_active`, `created_at`) VALUES
	(1, 1, 'GN01-K01', 'pw1001', '입구', 1, '2025-12-17 07:53:17'),
	(2, 1, 'GN01-K02', 'pw1002', '창가', 1, '2025-12-17 07:53:17'),
	(3, 2, 'HD01-K01', 'pw2001', '입구', 1, '2025-12-17 07:53:17'),
	(4, 2, 'HD01-K02', 'pw2002', '내부', 1, '2025-12-17 07:53:17'),
	(5, 3, 'SC01-K01', 'pw3001', '입구', 1, '2025-12-17 07:53:17'),
	(6, 3, 'SC01-K02', 'pw3002', '2층', 1, '2025-12-17 07:53:17'),
	(7, 4, 'JS01-K01', 'pw4001', '입구', 1, '2025-12-17 07:53:17'),
	(8, 4, 'JS01-K02', 'pw4002', '푸드코트', 1, '2025-12-17 07:53:17'),
	(9, 4, 'JS01-K03', 'pw4003', '외부', 1, '2025-12-17 07:53:17'),
	(10, 5, 'MD01-K01', 'pw5001', '1층', 1, '2025-12-17 07:53:17'),
	(11, 5, 'MD01-K02', 'pw5002', '2층', 1, '2025-12-17 07:53:17');

-- 테이블 nok_nok.menu_item 구조 내보내기
CREATE TABLE IF NOT EXISTS `menu_item` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category_id` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL COMMENT '메뉴 이름',
  `price` int(11) NOT NULL COMMENT '메뉴 가격',
  `is_active` tinyint(1) NOT NULL COMMENT '판매 여부',
  `image_url` varchar(255) DEFAULT NULL COMMENT '이미지 경로',
  PRIMARY KEY (`menu_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `menu_item_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- 테이블 데이터 nok_nok.menu_item:~55 rows (대략적) 내보내기
INSERT INTO `menu_item` (`menu_id`, `category_id`, `name`, `price`, `is_active`, `image_url`) VALUES
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
	(38, 4, '크로와상 샌드위치', 6500, 1, '/images/menu/croissant_sandwich.png'),
	(39, 4, '베이글 샌드위치', 6000, 1, '/images/menu/bagel_sandwich.png'),
	(40, 4, '에그 베네딕트', 8500, 1, '/images/menu/egg_benedict.png'),
	(41, 4, '팬케이크', 7500, 1, '/images/menu/pancake.png'),
	(42, 4, '와플', 7000, 1, '/images/menu/waffle.png'),
	(43, 4, '프렌치토스트', 7500, 1, '/images/menu/french_toast.png'),
	(44, 4, '샐러드', 8000, 1, '/images/menu/salad.png'),
	(45, 4, '아보카도 토스트', 8500, 1, '/images/menu/avocado_toast.png'),
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

-- 테이블 nok_nok.option_group 구조 내보내기
CREATE TABLE IF NOT EXISTS `option_group` (
  `option_group_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_id` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL COMMENT '그룹 이름',
  `is_required` tinyint(1) NOT NULL COMMENT '필수/비필수',
  `selection_type` varchar(20) NOT NULL COMMENT '단수/복수 선택',
  PRIMARY KEY (`option_group_id`),
  KEY `idx_option_group_menu` (`menu_id`),
  CONSTRAINT `option_group_ibfk_1` FOREIGN KEY (`menu_id`) REFERENCES `menu_item` (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- 테이블 데이터 nok_nok.option_group:~19 rows (대략적) 내보내기
INSERT INTO `option_group` (`option_group_id`, `menu_id`, `name`, `is_required`, `selection_type`) VALUES
	(1, 1, '사이즈', 1, 'SINGLE'),
	(2, 1, '얼음량', 0, 'SINGLE'),
	(3, 1, '샷 추가', 0, 'MULTI'),
	(4, 2, '사이즈', 1, 'SINGLE'),
	(5, 2, '온도', 1, 'SINGLE'),
	(6, 2, '샷 추가', 0, 'MULTI'),
	(7, 2, '시럽 추가', 0, 'MULTI'),
	(8, 3, '사이즈', 1, 'SINGLE'),
	(9, 3, '샷 추가', 0, 'MULTI'),
	(10, 4, '사이즈', 1, 'SINGLE'),
	(11, 4, '온도', 1, 'SINGLE'),
	(12, 16, '사이즈', 1, 'SINGLE'),
	(13, 16, '얼음량', 0, 'SINGLE'),
	(14, 17, '사이즈', 1, 'SINGLE'),
	(15, 19, '사이즈', 1, 'SINGLE'),
	(16, 19, '온도', 1, 'SINGLE'),
	(17, 38, '빵 종류', 1, 'SINGLE'),
	(18, 39, '빵 종류', 1, 'SINGLE'),
	(19, 41, '토핑', 0, 'MULTI');

-- 테이블 nok_nok.option_item 구조 내보내기
CREATE TABLE IF NOT EXISTS `option_item` (
  `option_item_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `option_group_id` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL COMMENT '옵션 이름',
  `option_price` int(11) NOT NULL COMMENT '옵션 가격',
  PRIMARY KEY (`option_item_id`),
  KEY `idx_option_item_group` (`option_group_id`),
  CONSTRAINT `option_item_ibfk_1` FOREIGN KEY (`option_group_id`) REFERENCES `option_group` (`option_group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- 테이블 데이터 nok_nok.option_item:~39 rows (대략적) 내보내기
INSERT INTO `option_item` (`option_item_id`, `option_group_id`, `name`, `option_price`) VALUES
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
	(15, 2, '얼음 적게', 0),
	(16, 2, '얼음 보통', 0),
	(17, 2, '얼음 많이', 0),
	(18, 13, '얼음 적게', 0),
	(19, 13, '얼음 보통', 0),
	(20, 13, '얼음 많이', 0),
	(21, 3, '샷 추가', 500),
	(22, 6, '샷 추가', 500),
	(23, 9, '샷 추가', 500),
	(24, 5, 'HOT', 0),
	(25, 5, 'ICED', 0),
	(26, 11, 'HOT', 0),
	(27, 11, 'ICED', 0),
	(28, 16, 'HOT', 0),
	(29, 16, 'ICED', 0),
	(30, 7, '바닐라 시럽', 500),
	(31, 7, '카라멜 시럽', 500),
	(32, 7, '헤이즐넛 시럽', 500),
	(33, 17, '크로와상', 0),
	(34, 17, '통밀빵', 500),
	(35, 18, '플레인 베이글', 0),
	(36, 18, '어니언 베이글', 500),
	(37, 19, '메이플 시럽', 0),
	(38, 19, '생크림', 1000),
	(39, 19, '과일', 1500);

-- 테이블 nok_nok.orders 구조 내보내기
CREATE TABLE IF NOT EXISTS `orders` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_id` bigint(20) NOT NULL,
  `store_id` bigint(20) NOT NULL,
  `order_no` int(11) NOT NULL COMMENT '주문번호',
  `status` tinyint(1) NOT NULL COMMENT '주문 완료/취소',
  `total_amount` int(11) NOT NULL COMMENT '총 가격',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() COMMENT '생성시간',
  `paid_at` timestamp NULL DEFAULT NULL COMMENT '결제 시간',
  `payment_method` varchar(20) DEFAULT NULL COMMENT '카드/현금/Pay/쿠폰',
  `payment_status` varchar(20) DEFAULT NULL COMMENT '결제 상태',
  `pg_transaction_id` varchar(50) DEFAULT NULL COMMENT 'PG사가 준 비식별 ID',
  PRIMARY KEY (`order_id`),
  KEY `idx_orders_store_created` (`store_id`,`created_at`),
  KEY `idx_orders_session` (`session_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`session_id`) REFERENCES `customer_session` (`session_id`),
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- 테이블 데이터 nok_nok.orders:~50 rows (대략적) 내보내기
INSERT INTO `orders` (`order_id`, `session_id`, `store_id`, `order_no`, `status`, `total_amount`, `created_at`, `paid_at`, `payment_method`, `payment_status`, `pg_transaction_id`) VALUES
	(1, 1, 1, 10001, 1, 5500, '2025-12-17 03:54:17', '2025-12-17 03:55:17', 'CARD', 'PAID', 'PG-20251210-00001'),
	(2, 2, 1, 10002, 1, 9000, '2025-12-17 03:59:17', '2025-12-17 04:00:17', 'CARD', 'PAID', 'PG-20251210-00002'),
	(3, 3, 2, 20001, 1, 5500, '2025-12-17 04:04:17', '2025-12-17 04:05:17', 'CARD', 'PAID', 'PG-20251210-00003'),
	(4, 4, 1, 10003, 1, 9500, '2025-12-17 04:09:17', '2025-12-17 04:10:17', 'CASH', 'PAID', 'PG-20251210-00004'),
	(5, 5, 2, 20002, 1, 11500, '2025-12-17 04:14:17', '2025-12-17 04:15:17', 'CARD', 'PAID', 'PG-20251210-00005'),
	(6, 6, 3, 30001, 1, 9500, '2025-12-17 04:19:17', '2025-12-17 04:20:17', 'CARD', 'PAID', 'PG-20251210-00006'),
	(7, 7, 1, 10004, 1, 6000, '2025-12-17 04:24:17', '2025-12-17 04:25:17', 'CARD', 'PAID', 'PG-20251210-00007'),
	(8, 8, 2, 20003, 1, 12000, '2025-12-17 04:29:17', '2025-12-17 04:30:17', 'CARD', 'PAID', 'PG-20251210-00008'),
	(9, 9, 3, 30002, 1, 8000, '2025-12-17 04:34:17', '2025-12-17 04:35:17', 'CASH', 'PAID', 'PG-20251210-00009'),
	(10, 10, 4, 40001, 1, 16500, '2025-12-17 04:39:17', '2025-12-17 04:40:17', 'CARD', 'PAID', 'PG-20251210-00010'),
	(11, 11, 2, 20004, 1, 9000, '2025-12-17 04:44:17', '2025-12-17 04:45:17', 'CARD', 'PAID', 'PG-20251210-00011'),
	(12, 12, 3, 30003, 1, 8500, '2025-12-17 04:49:17', '2025-12-17 04:50:17', 'CARD', 'PAID', 'PG-20251210-00012'),
	(13, 13, 4, 40002, 1, 11000, '2025-12-17 04:54:17', '2025-12-17 04:55:17', 'CARD', 'PAID', 'PG-20251210-00013'),
	(14, 14, 5, 50001, 1, 10000, '2025-12-17 04:59:17', '2025-12-17 05:00:17', 'CASH', 'PAID', 'PG-20251210-00014'),
	(15, 15, 1, 10005, 1, 8000, '2025-12-17 05:04:17', '2025-12-17 05:05:17', 'CARD', 'PAID', 'PG-20251210-00015'),
	(16, 16, 2, 20005, 1, 13700, '2025-12-17 05:09:17', '2025-12-17 05:10:17', 'CARD', 'PAID', 'PG-20251210-00016'),
	(17, 17, 3, 30004, 1, 5000, '2025-12-17 05:14:17', '2025-12-17 05:15:17', 'CARD', 'PAID', 'PG-20251210-00017'),
	(18, 18, 4, 40003, 1, 15000, '2025-12-17 05:19:17', '2025-12-17 05:20:17', 'CARD', 'PAID', 'PG-20251210-00018'),
	(19, 19, 5, 50002, 1, 4000, '2025-12-17 05:24:17', '2025-12-17 05:25:17', 'CARD', 'PAID', 'PG-20251210-00019'),
	(20, 20, 1, 10006, 1, 16000, '2025-12-17 05:29:17', '2025-12-17 05:30:17', 'CASH', 'PAID', 'PG-20251210-00020'),
	(21, 21, 2, 20006, 1, 11000, '2025-12-17 05:34:17', '2025-12-17 05:35:17', 'CARD', 'PAID', 'PG-20251210-00021'),
	(22, 22, 3, 30005, 1, 8300, '2025-12-17 05:39:17', '2025-12-17 05:40:17', 'CARD', 'PAID', 'PG-20251210-00022'),
	(23, 23, 4, 40004, 1, 13500, '2025-12-17 05:44:17', '2025-12-17 05:45:17', 'CARD', 'PAID', 'PG-20251210-00023'),
	(24, 24, 5, 50003, 1, 11000, '2025-12-17 05:49:17', '2025-12-17 05:50:17', 'CARD', 'PAID', 'PG-20251210-00024'),
	(25, 25, 1, 10007, 1, 13000, '2025-12-17 05:54:17', '2025-12-17 05:55:17', 'CARD', 'PAID', 'PG-20251210-00025'),
	(26, 26, 2, 20007, 1, 10500, '2025-12-17 05:59:17', '2025-12-17 06:00:17', 'CASH', 'PAID', 'PG-20251210-00026'),
	(27, 27, 3, 30006, 1, 8000, '2025-12-17 06:04:17', '2025-12-17 06:05:17', 'CARD', 'PAID', 'PG-20251210-00027'),
	(28, 28, 4, 40005, 1, 12500, '2025-12-17 06:09:17', '2025-12-17 06:10:17', 'CARD', 'PAID', 'PG-20251210-00028'),
	(29, 29, 5, 50004, 1, 8700, '2025-12-17 06:14:17', '2025-12-17 06:15:17', 'CARD', 'PAID', 'PG-20251210-00029'),
	(30, 30, 1, 10008, 1, 10500, '2025-12-17 06:19:17', '2025-12-17 06:20:17', 'CARD', 'PAID', 'PG-20251210-00030'),
	(31, 31, 2, 20008, 1, 9000, '2025-12-17 06:24:17', '2025-12-17 06:25:17', 'CARD', 'PAID', 'PG-20251210-00031'),
	(32, 32, 3, 30007, 1, 12000, '2025-12-17 06:29:17', '2025-12-17 06:30:17', 'CASH', 'PAID', 'PG-20251210-00032'),
	(33, 33, 4, 40006, 1, 9000, '2025-12-17 06:34:17', '2025-12-17 06:35:17', 'CARD', 'PAID', 'PG-20251210-00033'),
	(34, 34, 5, 50005, 1, 8200, '2025-12-17 06:39:17', '2025-12-17 06:40:17', 'CARD', 'PAID', 'PG-20251210-00034'),
	(35, 35, 1, 10009, 1, 13200, '2025-12-17 06:44:17', '2025-12-17 06:45:17', 'CARD', 'PAID', 'PG-20251210-00035'),
	(36, 36, 2, 20009, 1, 5000, '2025-12-17 06:49:17', '2025-12-17 06:50:17', 'CARD', 'PAID', 'PG-20251210-00036'),
	(37, 37, 3, 30008, 1, 10000, '2025-12-17 06:54:17', '2025-12-17 06:55:17', 'CARD', 'PAID', 'PG-20251210-00037'),
	(38, 38, 4, 40007, 1, 13000, '2025-12-17 06:59:17', '2025-12-17 07:00:17', 'CASH', 'PAID', 'PG-20251210-00038'),
	(39, 39, 5, 50006, 1, 9500, '2025-12-17 07:04:17', '2025-12-17 07:05:17', 'CARD', 'PAID', 'PG-20251210-00039'),
	(40, 40, 1, 10010, 1, 10000, '2025-12-17 07:09:17', '2025-12-17 07:10:17', 'CARD', 'PAID', 'PG-20251210-00040'),
	(41, 41, 2, 20010, 1, 8000, '2025-12-17 07:14:17', '2025-12-17 07:15:17', 'CARD', 'PAID', 'PG-20251210-00041'),
	(42, 42, 3, 30009, 1, 12000, '2025-12-17 07:19:17', '2025-12-17 07:20:17', 'CARD', 'PAID', 'PG-20251210-00042'),
	(43, 43, 4, 40008, 1, 7000, '2025-12-17 07:24:17', '2025-12-17 07:25:17', 'CASH', 'PAID', 'PG-20251210-00043'),
	(44, 44, 5, 50007, 1, 11000, '2025-12-17 07:29:17', '2025-12-17 07:30:17', 'CARD', 'PAID', 'PG-20251210-00044'),
	(45, 45, 1, 10011, 1, 12500, '2025-12-17 07:34:17', '2025-12-17 07:35:17', 'CARD', 'PAID', 'PG-20251210-00045'),
	(46, 46, 2, 20011, 1, 8500, '2025-12-17 07:39:17', '2025-12-17 07:40:17', 'CARD', 'PAID', 'PG-20251210-00046'),
	(47, 47, 3, 30010, 1, 9800, '2025-12-17 07:44:17', '2025-12-17 07:45:17', 'CARD', 'PAID', 'PG-20251210-00047'),
	(48, 48, 4, 40009, 1, 8500, '2025-12-17 07:46:17', '2025-12-17 07:47:17', 'CASH', 'PAID', 'PG-20251210-00048'),
	(49, 49, 5, 50008, 1, 12200, '2025-12-17 07:48:17', '2025-12-17 07:49:17', 'CARD', 'PAID', 'PG-20251210-00049'),
	(50, 50, 1, 10012, 1, 10000, '2025-12-17 07:51:17', '2025-12-17 07:52:17', 'CARD', 'PAID', 'PG-20251210-00050');

-- 테이블 nok_nok.order_item 구조 내보내기
CREATE TABLE IF NOT EXISTS `order_item` (
  `order_item_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL,
  `menu_id` bigint(20) NOT NULL,
  `quantity` int(11) NOT NULL COMMENT '수량',
  `unit_price` int(11) NOT NULL COMMENT '개별 금액',
  `line_amount` int(11) NOT NULL COMMENT '총 금액',
  PRIMARY KEY (`order_item_id`),
  KEY `idx_order_item_order` (`order_id`),
  KEY `idx_order_item_menu` (`menu_id`),
  CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `order_item_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `menu_item` (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- 테이블 데이터 nok_nok.order_item:~91 rows (대략적) 내보내기
INSERT INTO `order_item` (`order_item_id`, `order_id`, `menu_id`, `quantity`, `unit_price`, `line_amount`) VALUES
	(1, 1, 1, 1, 4000, 5500),
	(2, 2, 2, 2, 4500, 9000),
	(3, 3, 28, 1, 5500, 5500),
	(4, 4, 1, 1, 4000, 4000),
	(5, 4, 28, 1, 5500, 5500),
	(6, 5, 2, 1, 4500, 6000),
	(7, 5, 29, 1, 6000, 6000),
	(8, 6, 16, 1, 5000, 5000),
	(9, 6, 30, 1, 4500, 4500),
	(10, 7, 3, 1, 4500, 6000),
	(11, 8, 4, 1, 5000, 5000),
	(12, 8, 31, 1, 7000, 7000),
	(13, 9, 1, 2, 4000, 8000),
	(14, 10, 5, 1, 5000, 5500),
	(15, 10, 19, 1, 5500, 5500),
	(16, 10, 32, 1, 3500, 3500),
	(17, 11, 6, 1, 5000, 5000),
	(18, 11, 36, 1, 4000, 4000),
	(19, 12, 7, 1, 3500, 3500),
	(20, 12, 34, 1, 5000, 5000),
	(21, 13, 38, 1, 6500, 6500),
	(22, 13, 2, 1, 4500, 4500),
	(23, 14, 8, 1, 4500, 4500),
	(24, 14, 28, 1, 5500, 5500),
	(25, 15, 1, 2, 4000, 8000),
	(26, 16, 17, 1, 5200, 5200),
	(27, 16, 16, 1, 5000, 5000),
	(28, 16, 33, 1, 3500, 3500),
	(29, 17, 21, 1, 5000, 5000),
	(30, 18, 41, 1, 7500, 10500),
	(31, 18, 2, 1, 4500, 4500),
	(32, 19, 1, 1, 4000, 4000),
	(33, 20, 2, 1, 4500, 4500),
	(34, 20, 28, 1, 5500, 5500),
	(35, 20, 29, 1, 6000, 6000),
	(36, 21, 19, 1, 5500, 5500),
	(37, 21, 28, 1, 5500, 5500),
	(38, 22, 9, 1, 4800, 4800),
	(39, 22, 48, 1, 3500, 3500),
	(40, 23, 10, 1, 5500, 6500),
	(41, 23, 31, 1, 7000, 7000),
	(42, 24, 39, 1, 6000, 6000),
	(43, 24, 20, 1, 5000, 5000),
	(44, 25, 40, 1, 8500, 8500),
	(45, 25, 2, 1, 4500, 4500),
	(46, 26, 28, 1, 5500, 5500),
	(47, 26, 1, 1, 4000, 5000),
	(48, 27, 1, 2, 4000, 8000),
	(49, 28, 42, 1, 7000, 7000),
	(50, 28, 19, 1, 5500, 5500),
	(51, 29, 11, 1, 5200, 5200),
	(52, 29, 46, 1, 3500, 3500),
	(53, 30, 2, 1, 4500, 4500),
	(54, 30, 29, 1, 6000, 6000),
	(55, 31, 1, 2, 4000, 9000),
	(56, 32, 43, 1, 7500, 7500),
	(57, 32, 2, 1, 4500, 4500),
	(58, 33, 4, 1, 5000, 5000),
	(59, 33, 35, 1, 4000, 4000),
	(60, 34, 12, 1, 5200, 5200),
	(61, 34, 47, 1, 3000, 3000),
	(62, 35, 44, 1, 8000, 8000),
	(63, 35, 17, 1, 5200, 5200),
	(64, 36, 16, 1, 5000, 5000),
	(65, 37, 2, 1, 4500, 4500),
	(66, 37, 28, 1, 5500, 5500),
	(67, 38, 45, 1, 8500, 8500),
	(68, 38, 2, 1, 4500, 4500),
	(69, 39, 20, 1, 5000, 5000),
	(70, 39, 30, 1, 4500, 4500),
	(71, 40, 2, 1, 4500, 4500),
	(72, 40, 28, 1, 5500, 5500),
	(73, 41, 1, 2, 4000, 8000),
	(74, 42, 14, 1, 5000, 6000),
	(75, 42, 29, 1, 6000, 6000),
	(76, 43, 24, 1, 4500, 4500),
	(77, 43, 49, 1, 2500, 2500),
	(78, 44, 22, 1, 5500, 5500),
	(79, 44, 28, 1, 5500, 5500),
	(80, 45, 15, 1, 5500, 5500),
	(81, 45, 31, 1, 7000, 7000),
	(82, 46, 6, 1, 5000, 5000),
	(83, 46, 48, 1, 3500, 3500),
	(84, 47, 23, 1, 4800, 4800),
	(85, 47, 34, 1, 5000, 5000),
	(86, 48, 2, 1, 4500, 4500),
	(87, 48, 36, 1, 4000, 4000),
	(88, 49, 18, 1, 5200, 5200),
	(89, 49, 31, 1, 7000, 7000),
	(90, 50, 2, 1, 4500, 4500),
	(91, 50, 28, 1, 5500, 5500);

-- 테이블 nok_nok.order_item_option 구조 내보내기
CREATE TABLE IF NOT EXISTS `order_item_option` (
  `order_item_option_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_item_id` bigint(20) NOT NULL,
  `option_item_id` bigint(20) NOT NULL,
  `extra_price` int(11) NOT NULL COMMENT '옵션 금액',
  `option_quantity` int(11) NOT NULL COMMENT '옵션 수량',
  PRIMARY KEY (`order_item_option_id`),
  KEY `order_item_id` (`order_item_id`),
  KEY `option_item_id` (`option_item_id`),
  CONSTRAINT `order_item_option_ibfk_1` FOREIGN KEY (`order_item_id`) REFERENCES `order_item` (`order_item_id`),
  CONSTRAINT `order_item_option_ibfk_2` FOREIGN KEY (`option_item_id`) REFERENCES `option_item` (`option_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- 테이블 데이터 nok_nok.order_item_option:~12 rows (대략적) 내보내기
INSERT INTO `order_item_option` (`order_item_option_id`, `order_item_id`, `option_item_id`, `extra_price`, `option_quantity`) VALUES
	(1, 1, 2, 500, 1),
	(2, 1, 21, 500, 2),
	(3, 6, 22, 500, 3),
	(4, 10, 6, 500, 1),
	(5, 10, 23, 500, 2),
	(6, 14, 22, 500, 1),
	(7, 30, 38, 1000, 1),
	(8, 30, 39, 1500, 2),
	(9, 40, 21, 500, 2),
	(10, 47, 21, 500, 2),
	(11, 55, 21, 500, 2),
	(12, 74, 22, 500, 2);

-- 테이블 nok_nok.store 구조 내보내기
CREATE TABLE IF NOT EXISTS `store` (
  `store_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '지점명',
  `code` varchar(30) NOT NULL COMMENT 'GN01, HD01 지점 코드',
  `address` varchar(255) DEFAULT NULL COMMENT '매장주소',
  `is_active` tinyint(1) NOT NULL COMMENT '활성화 여부',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`store_id`),
  UNIQUE KEY `uk_store_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- 테이블 데이터 nok_nok.store:~5 rows (대략적) 내보내기
INSERT INTO `store` (`store_id`, `name`, `code`, `address`, `is_active`, `created_at`) VALUES
	(1, '강남 1호점', 'GN01', '서울시 강남구 테헤란로 123', 1, '2025-12-17 07:53:16'),
	(2, '홍대 1호점', 'HD01', '서울시 마포구 홍대입구로 45', 1, '2025-12-17 07:53:16'),
	(3, '신촌 1호점', 'SC01', '서울시 서대문구 신촌역로 89', 1, '2025-12-17 07:53:16'),
	(4, '잠실 1호점', 'JS01', '서울시 송파구 올림픽로 567', 1, '2025-12-17 07:53:16'),
	(5, '명동 1호점', 'MD01', '서울시 중구 명동길 234', 1, '2025-12-17 07:53:16');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
