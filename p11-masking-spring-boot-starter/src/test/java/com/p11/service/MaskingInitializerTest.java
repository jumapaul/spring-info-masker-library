package com.p11.service;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.p11.config.MaskOptions;
import com.p11.config.MaskingProperties;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class MaskingInitializerTest {

    @Test
    void testInitSetsMaskingService() {
        MaskingService mockService = Mockito.mock(MaskingService.class);
        MaskingInitializer initializer = new MaskingInitializer(mockService);

        initializer.init();

        ILoggingEvent event = Mockito.mock(ILoggingEvent.class);
        Mockito.when(event.getFormattedMessage()).thenReturn("test message");
        Mockito.when(mockService.mask("test message")).thenReturn("masked");

        assertEquals("masked", new MaskingConverter().convert(event));
    }

    @Test
    void testInitWithRealService() {
        MaskingProperties props = new MaskingProperties();
        props.setMaskStyle(MaskOptions.FULL);
        props.setMaskCharacter("*");
        props.setFields(java.util.List.of("password"));

        MaskingService realService = new MaskingServiceImpl(props);
        MaskingInitializer initializer = new MaskingInitializer(realService);

        initializer.init();

        ILoggingEvent event = Mockito.mock(ILoggingEvent.class);
        Mockito.when(event.getFormattedMessage()).thenReturn("{\"password\":\"secret\"}");

        String result = new MaskingConverter().convert(event);
        assertEquals("{\"password\":\"******\"}", result);
    }
}