package com.NOK_NOK.order.exceptions;

/**
 * 메뉴 관련 예외
 */
public class MenuExceptions {

    /**
     * 메뉴를 찾을 수 없음
     * HTTP 404
     */
    public static class MenuNotFoundException extends RuntimeException {
        public MenuNotFoundException(Long menuId) {
            super("메뉴를 찾을 수 없습니다. menuId: " + menuId);
        }
    }

    /**
     * 메뉴가 비활성화 상태
     * HTTP 400
     */
    public static class MenuNotActiveException extends RuntimeException {
        public MenuNotActiveException(Long menuId) {
            super("현재 판매 중이지 않은 메뉴입니다. menuId: " + menuId);
        }
    }

    /**
     * 유효하지 않은 옵션 선택
     * HTTP 400
     */
    public static class InvalidOptionException extends RuntimeException {
        public InvalidOptionException(String message) {
            super(message);
        }
    }
}
