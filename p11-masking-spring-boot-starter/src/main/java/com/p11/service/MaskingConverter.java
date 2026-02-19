package com.p11.service;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.Setter;

public class MaskingConverter extends ClassicConverter {

    @Setter
    private static MaskingService maskingService;

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        String message = iLoggingEvent.getFormattedMessage();

        if (maskingService != null && message != null) {
            return maskingService.mask(message);
        }
        return message;
    }
}
