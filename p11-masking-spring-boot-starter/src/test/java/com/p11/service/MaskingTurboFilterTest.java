package com.p11.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.spi.FilterReply;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class MaskingTurboFilterTest {

    @Test
    void testFilterNotStarted() {
        MaskingTurboFilter filter = new MaskingTurboFilter();
        filter.start();
        filter.stop();

        MaskingService mockService = Mockito.mock(MaskingService.class);
        filter.setApplicationContext(Mockito.mock(org.springframework.context.ApplicationContext.class));
        Mockito.when(mockService.mayNeedMasking(Mockito.anyString())).thenReturn(true);

        assertEquals(FilterReply.NEUTRAL,
                filter.decide(null, Mockito.mock(Logger.class), Level.INFO, "msg", new Object[]{"secret"}, null));
    }

    @Test
    void testNoMaskingService() {
        MaskingTurboFilter filter = new MaskingTurboFilter();
        filter.start();

        assertEquals(FilterReply.NEUTRAL,
                filter.decide(null, Mockito.mock(Logger.class), Level.INFO, "msg", new Object[]{"secret"}, null));
    }

    @Test
    void testAcceptWhenSensitiveParam() throws IllegalAccessException, NoSuchFieldException {
        MaskingTurboFilter filter = new MaskingTurboFilter();
        filter.start();

        MaskingService mockService = Mockito.mock(MaskingService.class);
        Mockito.when(mockService.mayNeedMasking("secret")).thenReturn(true);
        filter.setApplicationContext(Mockito.mock(org.springframework.context.ApplicationContext.class));
        java.lang.reflect.Field f = MaskingTurboFilter.class.getDeclaredField("maskingService");
        f.setAccessible(true);
        f.set(filter, mockService);

        assertEquals(FilterReply.NEUTRAL,
                filter.decide(null, Mockito.mock(Logger.class), Level.INFO, "msg", new Object[]{"secret"}, null));
    }

    @Test
    void testNeutralWhenNoSensitiveParam() throws Exception {
        MaskingTurboFilter filter = new MaskingTurboFilter();
        filter.start();

        MaskingService mockService = Mockito.mock(MaskingService.class);
        Mockito.when(mockService.mayNeedMasking("normal")).thenReturn(false);

        java.lang.reflect.Field f = MaskingTurboFilter.class.getDeclaredField("maskingService");
        f.setAccessible(true);
        f.set(filter, mockService);

        assertEquals(FilterReply.NEUTRAL,
                filter.decide(null, Mockito.mock(Logger.class), Level.INFO, "msg", new Object[]{"normal"}, null));
    }
}