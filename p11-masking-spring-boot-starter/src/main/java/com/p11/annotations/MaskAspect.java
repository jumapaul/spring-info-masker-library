package com.p11.annotations;

import com.p11.service.MaskingService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class MaskAspect {

    private final MaskingService maskingService;

    @Around("@annotation(maskAnnotation)")
    public Object maskMethodReturn(ProceedingJoinPoint joinPoint, Mask maskAnnotation) throws Throwable {
        Object result = joinPoint.proceed();

        if (result instanceof String string) {
            Mask.MaskOptions option = maskAnnotation.options();

            if (option == Mask.MaskOptions.DEFAULT) {
                return maskingService.mask(string);
            } else {
                return switch (option) {
                    case FULL -> maskingService.mask(string);
                    case PARTIAL -> maskingService.maskPartially(string);
                    case LAST4 -> maskingService.maskLast4(string);
                    default -> string;
                };
            }
        }

        return result;
    }
}
