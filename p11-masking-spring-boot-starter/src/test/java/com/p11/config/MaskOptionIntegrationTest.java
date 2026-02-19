package com.p11.config;

import com.multi_threading.bankmaskingspringbootstarter.service.MaskingServiceImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MaskOptionIntegrationTest {

    @ParameterizedTest
    @EnumSource(MaskOptions.class)
    void testMaskingStyles(MaskOptions style) {
        MaskingProperties props = new MaskingProperties();
        props.setMaskStyle(style);
        props.setMaskCharacter("*");
        props.setFields(java.util.List.of("field"));

        MaskingServiceImpl service = new MaskingServiceImpl(props);

        String input = "{\"field\":\"123456789\"}";
        String result = service.mask(input);

        switch (style) {
            case FULL -> assertEquals("{\"field\":\"*********\"}", result);
            case PARTIAL -> assertEquals("{\"field\":\"123***789\"}", result);
            case LAST4 -> assertEquals("{\"field\":\"*****6789\"}", result);
        }
    }
}
