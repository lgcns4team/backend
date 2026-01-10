package com.NOK_NOK.order.exceptions;

/**
 * 주문 관련 Exception
 */
public class OrderExceptions {

    /**
     * 세션을 찾을 수 없음
     */
    public static class SessionNotFoundException extends RuntimeException {
        public SessionNotFoundException(Long sessionId) {
            super(String.format("세션을 찾을 수 없습니다. sessionId: %d", sessionId));
        }
    }

    /**
     * 매장을 찾을 수 없음
     */
    public static class StoreNotFoundException extends RuntimeException {
        public StoreNotFoundException(Long storeId) {
            super(String.format("매장을 찾을 수 없습니다. storeId: %d", storeId));
        }
    }

    /**
     * 주문을 찾을 수 없음
     */
    public static class OrderNotFoundException extends RuntimeException {
        public OrderNotFoundException(Long orderId) {
            super(String.format("주문을 찾을 수 없습니다. orderId: %d", orderId));
        }
    }

    /**
     * 메뉴를 찾을 수 없음
     */
    public static class MenuNotFoundException extends RuntimeException {
        public MenuNotFoundException(Long menuId) {
            super(String.format("메뉴를 찾을 수 없습니다. menuId: %d", menuId));
        }
    }

    /**
     * 옵션을 찾을 수 없음
     */
    public static class OptionNotFoundException extends RuntimeException {
        public OptionNotFoundException(Long optionItemId) {
            super(String.format("옵션을 찾을 수 없습니다. optionItemId: %d", optionItemId));
        }
    }

    /**
     * 가격 검증 실패
     */
    public static class PriceValidationException extends RuntimeException {
        public PriceValidationException(Integer expected, Integer calculated) {
            super(String.format("가격 검증 실패. 예상: %d원, 계산: %d원", expected, calculated));
        }
    }

    /**
     * 비활성화된 메뉴
     */
    public static class MenuNotActiveException extends RuntimeException {
        public MenuNotActiveException(Long menuId) {
            super(String.format("비활성화된 메뉴입니다. menuId: %d", menuId));
        }
    }

    /**
     * 잘못된 주문 요청
     */
    public static class InvalidOrderRequestException extends RuntimeException {
        public InvalidOrderRequestException(String message) {
            super(message);
        }
    }
}
