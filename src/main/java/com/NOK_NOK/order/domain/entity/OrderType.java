package com.NOK_NOK.order.domain.entity;

/**
 * 주문 유형
 */
public enum OrderType {
    DINE_IN,   // 매장 식사
    TAKEOUT;   // 포장
    
    /**
     * DB 값으로 변환
     * DINE_IN → "dine-in"
     * TAKEOUT → "takeout"
     */
    public String toDbValue() {
        return this.name().toLowerCase().replace('_', '-');
    }
    
    /**
     * DB 값에서 Enum으로 변환
     * "dine-in" → DINE_IN
     * "takeout" → TAKEOUT
     */
    public static OrderType fromDbValue(String dbValue) {
        if (dbValue == null) {
            return DINE_IN;
        }
        
        String enumName = dbValue.toUpperCase().replace('-', '_');
        try {
            return OrderType.valueOf(enumName);
        } catch (IllegalArgumentException e) {
            return DINE_IN; // 잘못된 값은 기본값
        }
    }
}