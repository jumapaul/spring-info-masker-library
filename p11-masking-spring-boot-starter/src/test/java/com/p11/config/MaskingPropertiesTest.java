package com.p11.config;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaskingPropertiesTest {

    @Test
    void testDefaultValues() {
        MaskingProperties props = new MaskingProperties();
        assertTrue(props.isEnabled());
        assertTrue(props.getFields().isEmpty());
        assertEquals(MaskOptions.FULL, props.getMaskStyle());
        assertEquals("*", props.getMaskCharacter());
    }

    @Test
    void testCustomValues() {
        MaskingProperties props = new MaskingProperties();
        props.setEnabled(false);
        props.setFields(List.of("password", "token"));
        props.setMaskStyle(MaskOptions.LAST4);
        props.setMaskCharacter("#");

        assertFalse(props.isEnabled());
        assertEquals(List.of("password", "token"), props.getFields());
        assertEquals(MaskOptions.LAST4, props.getMaskStyle());
        assertEquals("#", props.getMaskCharacter());
    }
}