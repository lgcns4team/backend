package com.NOK_NOK.abs.exceptions;

/**
 * 광고 관련 Exception
 */
public class AdExceptions {

    /**
     * 광고를 찾을 수 없음
     */
    public static class AdNotFoundException extends RuntimeException {
        public AdNotFoundException(Long adId) {
            super(String.format("광고를 찾을 수 없습니다. adId: %d", adId));
        }
    }

    /**
     * 세션을 찾을 수 없음
     */
    public static class SessionNotFoundException extends RuntimeException {
        public SessionNotFoundException(Long sessionId) {
            super(String.format("세션을 찾을 수 없습니다. sessionId: %d", sessionId));
        }
    }

    /**
     * 활성화된 광고가 없음
     */
    public static class NoActiveAdsException extends RuntimeException {
        public NoActiveAdsException() {
            super("활성화된 광고가 없습니다.");
        }
    }

    /**
     * 잘못된 광고 로그 요청
     */
    public static class InvalidDisplayLogRequestException extends RuntimeException {
        public InvalidDisplayLogRequestException(String message) {
            super(message);
        }
    }
}