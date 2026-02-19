package com.p11.service;

public interface MaskingService {

    String mask(String fieldValue);

    String maskLast4(String fieldValue);

    String maskPartially(String fieldValue);

    default boolean mayNeedMasking(String fieldValue) {
        return fieldValue != null && !fieldValue.isBlank();
    }
}
