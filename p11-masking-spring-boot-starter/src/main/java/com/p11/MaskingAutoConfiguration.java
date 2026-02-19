package com.p11;

import ch.qos.logback.classic.LoggerContext;
import com.p11.config.MaskingProperties;
import com.p11.service.MaskingTurboFilter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@Configuration
@EnableConfigurationProperties(MaskingProperties.class)
@RequiredArgsConstructor
@Slf4j
public class MaskingAutoConfiguration {
    private final MaskingProperties properties;

    @PostConstruct
    public void init() {
        log.info("Masking initialized. Enabled: {}, Fields: {}", properties.isEnabled(), properties.getFields());
    }

    @Bean
    @ConditionalOnProperty(prefix = "p11.masking", name = "enabled", havingValue = "true")
    public MaskingTurboFilter maskingTurboFilter() {
        return new MaskingTurboFilter();
    }

    @EventListener(ContextRefreshedEvent.class)
    @DependsOn("maskingTurboFilter")
    public void registerTurboFilter() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        MaskingTurboFilter filter = maskingTurboFilter();
        filter.start();
        context.addTurboFilter(filter);
    }
}