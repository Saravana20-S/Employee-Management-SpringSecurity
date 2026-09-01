package com.bridgelabz.employeemanagement.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExecutionTimeAspect {

    @Around(
            "execution(* com.bridgelabz.employeemanagement.service.impl..*(..))"
    )
    public Object measureExecutionTime(
            ProceedingJoinPoint joinPoint)
            throws Throwable {

        long startTime =
                System.currentTimeMillis();

        try {

            return joinPoint.proceed();

        } finally {

            long executionTime =
                    System.currentTimeMillis()
                            - startTime;

            log.info(
                    "Method {} executed in {} ms",
                    joinPoint.getSignature().toShortString(),
                    executionTime
            );
        }
    }
}