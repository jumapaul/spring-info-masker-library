package com.p11.service;

import com.p11.config.MaskOptions;
import com.p11.config.MaskingProperties;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MaskingServiceImplTest {

    private MaskingProperties props(String style, String... fields) {
        MaskingProperties p = new MaskingProperties();
        p.setMaskStyle(MaskOptions.valueOf(style));
        p.setMaskCharacter("*");
        p.setFields(Arrays.asList(fields));
        return p;
    }

    @Test
    void testFullMaskingJsonField() {
        MaskingServiceImpl service = new MaskingServiceImpl(
                props("FULL", "password")
        );
        String input = "{\"username\":\"john\",\"password\":\"secret123\"}";
        String expected = "{\"username\":\"john\",\"password\":\"*********\"}";
        assertEquals(expected, service.mask(input));
    }

    @Test
    void testLast4MaskingObjectField() {
        MaskingServiceImpl service = new MaskingServiceImpl(
                props("LAST4", "accountNumber")
        );
        String input = "User(accountNumber=123456789, name=John)";
        String expected = "User(accountNumber=*****6789, name=John)";
        assertEquals(expected, service.mask(input));
    }

    @Test
    void testMightNeedMasking() {
        MaskingServiceImpl service = new MaskingServiceImpl(
                props("FULL", "password")
        );
        assertTrue(service.mayNeedMasking("user password=12345"));
        assertFalse(service.mayNeedMasking("abc"));
        assertFalse(service.mayNeedMasking(null));
    }
}