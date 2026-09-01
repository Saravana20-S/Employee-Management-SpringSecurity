package com.bridgelabz.employeemanagement.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before(
            "execution(* com.bridgelabz.employeemanagement.service.impl..*(..))"
    )
    public void logBefore(
            JoinPoint joinPoint) {

        log.info(
                "Executing method: {}",
                joinPoint.getSignature().toShortString()
        );
    }

    @AfterReturning(
            pointcut =
                    "execution(* com.bridgelabz.employeemanagement.service.impl..*(..))"
    )
    public void logSuccess(
            JoinPoint joinPoint) {

        log.info(
                "Successfully completed: {}",
                joinPoint.getSignature().toShortString()
        );
    }

    @AfterThrowing(
            pointcut =
                    "execution(* com.bridgelabz.employeemanagement.service.impl..*(..))",
            throwing = "exception"
    )
    public void logFailure(
            JoinPoint joinPoint,
            Throwable exception) {

        log.error(
                "Failed method: {} | Error: {}",
                joinPoint.getSignature().toShortString(),
                exception.getMessage()
        );
    }
}