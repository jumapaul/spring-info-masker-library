package com.p11.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class MaskingTurboFilter extends TurboFilter implements ApplicationContextAware {

    private MaskingService maskingService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.maskingService = applicationContext.getBean(MaskingService.class);
    }

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable throwable) {
        if (!isStarted() || maskingService == null || format == null || params == null) {
            return FilterReply.NEUTRAL;
        }

        //Check if format string for field names
        if (maskingService.mayNeedMasking(format)) {
            return FilterReply.NEUTRAL;
        }

        // String params
        for (Object param : params) {
            String stringParams = (param != null) ? param.toString() : null;

            if (stringParams != null && maskingService.mayNeedMasking(stringParams)) {
                return FilterReply.NEUTRAL;
            }
        }
        return FilterReply.NEUTRAL;
    }
}
