package com.p11.service;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class MaskingConverterTest {

    @Test
    void testNoMaskingService() {
        MaskingConverter.setMaskingService(null);
        ILoggingEvent event = Mockito.mock(ILoggingEvent.class);
        Mockito.when(event.getFormattedMessage()).thenReturn("plain message");

        assertEquals("plain message", new MaskingConverter().convert(event));
    }

    @Test
    void testWithMaskingService() {
        MaskingService mockService = Mockito.mock(MaskingService.class);
        Mockito.when(mockService.mask("secret message")).thenReturn("masked message");
        MaskingConverter.setMaskingService(mockService);

        ILoggingEvent event = Mockito.mock(ILoggingEvent.class);
        Mockito.when(event.getFormattedMessage()).thenReturn("secret message");

        assertEquals("masked message", new MaskingConverter().convert(event));
    }

    @Test
    void testNullMessage() {
        MaskingService mockService = Mockito.mock(MaskingService.class);
        MaskingConverter.setMaskingService(mockService);

        ILoggingEvent event = Mockito.mock(ILoggingEvent.class);
        Mockito.when(event.getFormattedMessage()).thenReturn(null);

        assertNull(new MaskingConverter().convert(event));
        Mockito.verify(mockService, Mockito.never()).mask(Mockito.any());
    }
}