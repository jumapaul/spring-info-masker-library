package com.p11.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class MaskingInitializer {

    private final MaskingService maskingService;

    public MaskingInitializer(MaskingService maskingService) {
        this.maskingService = maskingService;
    }

    @PostConstruct
    public void init() {
        MaskingConverter.setMaskingService(maskingService);
    }
}
