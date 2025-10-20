package org.gamebackend.aspect;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Around("execution(* org.gamebackend..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.trace("Before {}.{}, args: {}", joinPoint.getSignature().getDeclaringType().getSimpleName(),
                    joinPoint.getSignature().getName(), joinPoint.getArgs());
            Object obj = joinPoint.proceed();
            log.trace("After {}, returns: {}", joinPoint.getSignature().getName(), obj);
            return obj;
        } catch (Exception e) {
            log.trace("Error after {}: {}", joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }
    }
}
