package com.NOK_NOK.order.domain.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * OrderType Enum ↔ DB String 변환
 * 
 * Java: DINE_IN, TAKEOUT
 * DB:   "dine-in", "takeout"
 */
@Converter(autoApply = true)
public class OrderTypeConverter implements AttributeConverter<OrderType, String> {

    @Override
    public String convertToDatabaseColumn(OrderType attribute) {
        if (attribute == null) {
            return OrderType.DINE_IN.toDbValue();
        }
        return attribute.toDbValue();
    }

    @Override
    public OrderType convertToEntityAttribute(String dbData) {
        return OrderType.fromDbValue(dbData);
    }
}