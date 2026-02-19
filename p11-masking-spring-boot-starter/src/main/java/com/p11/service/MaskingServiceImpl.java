package com.p11.service;

import com.p11.config.MaskingProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class MaskingServiceImpl implements MaskingService {

    private final MaskingProperties properties;
    private final Map<String, Pattern[]> definedPattern;

    public MaskingServiceImpl(MaskingProperties properties) {
        this.properties = properties;
        this.definedPattern = properties.getFields().stream()
                .collect(Collectors.toUnmodifiableMap(
                        field -> field,
                        field -> new Pattern[]{
                                Pattern.compile("(\"" + Pattern.quote(field) + "[^\"]*\":\")([^\"]++)(\")"),
                                Pattern.compile("(" + Pattern.quote(field) + "[^=]*=)([^,)]+)")
                        }
                ));
    }

    @Override
    public String mask(String fieldValue) {
        if (fieldValue == null || fieldValue.isBlank()) return fieldValue;

        String masked = fieldValue;

        for (Map.Entry<String, Pattern[]> entry : definedPattern.entrySet()) {
            masked = applyMask(masked, entry.getValue()[0]);
            masked = applyMask(masked, entry.getValue()[1]);
        }
        return masked;
    }

    @Override
    public String maskLast4(String fieldValue) {
        int valueLength = fieldValue.length();
        if (valueLength <= 4) {
            //If length is less or equal to 4, perform full mask.
            return properties.getMaskCharacter().repeat(4);
        }
        return properties.getMaskCharacter().repeat(valueLength - 4) + fieldValue.substring(valueLength - 4);
    }

    @Override
    public String maskPartially(String fieldValue) {
        if (fieldValue.length() <= 6) {
            return properties.getMaskCharacter().repeat(fieldValue.length());
        }

        int mid = fieldValue.length() / 2;
        int start = mid - 1;
        int end = mid + 2;

        return fieldValue.substring(0, start)
                + properties.getMaskCharacter().repeat(3)
                + fieldValue.substring(end);
    }

    @Override
    public boolean mayNeedMasking(String fieldValue) {
        if (fieldValue == null || fieldValue.length() < 4) return false;

        return definedPattern.values().stream()
                .flatMap(Arrays::stream)
                .anyMatch(pattern -> pattern.matcher(fieldValue).find());
    }

    private String applyMask(String value, Pattern pattern) {
        Matcher matcher = pattern.matcher(value);
        StringBuilder stringBuilder = new StringBuilder();

        while (matcher.find()) {
            String initialValue = matcher.group(2);
            String maskedValue = maskValue(initialValue);
            String newValue = matcher.group(1) + maskedValue + (matcher.groupCount() > 2 ? matcher.group(3) : "");
            matcher.appendReplacement(stringBuilder, Matcher.quoteReplacement(newValue));
        }

        matcher.appendTail(stringBuilder);
        return stringBuilder.toString();
    }

    private String maskValue(String value) {
        return switch (properties.getMaskStyle()) {
            case FULL -> properties.getMaskCharacter().repeat(value.length());
            case LAST4 -> maskLast4(value);
            case PARTIAL -> maskPartially(value);
        };
    }
}
