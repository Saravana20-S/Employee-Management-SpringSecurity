package com.bridgelabz.employeemanagement.aspect;

import com.bridgelabz.employeemanagement.entity.User;
import com.bridgelabz.employeemanagement.repository.UserRepository;
import com.bridgelabz.employeemanagement.service.AuditService;
import com.bridgelabz.employeemanagement.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;
    private final UserRepository userRepository;

    @Around(
            "execution(* com.bridgelabz.employeemanagement.service.impl..*(..))" +
                    " && !execution(* com.bridgelabz.employeemanagement.service.impl.AuditServiceImpl.*(..))"
    )
    public Object createAuditLog(
            ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();

        String status;

        try {

            Object result = joinPoint.proceed();

            status = "SUCCESS";

            saveAuditIfUserExists(
                    joinPoint,
                    startTime,
                    status
            );

            return result;

        } catch (Throwable exception) {

            status = "FAILED";

            saveAuditIfUserExists(
                    joinPoint,
                    startTime,
                    status
            );

            throw exception;
        }
    }

    private void saveAuditIfUserExists(
            ProceedingJoinPoint joinPoint,
            long startTime,
            String status) {

        long executionTime =
                System.currentTimeMillis() - startTime;

        String email =
                SecurityUtil.getCurrentUserEmail();

        if (email != null) {

            User user = userRepository
                    .findByEmail(email)
                    .orElse(null);

            if (user != null) {

                saveAudit(
                        user,
                        joinPoint,
                        executionTime,
                        status
                );
            }
        }
    }

    private void saveAudit(
            User user,
            ProceedingJoinPoint joinPoint,
            long executionTime,
            String status) {

        auditService.saveAuditLog(
                user,
                joinPoint.getSignature().getName(),
                joinPoint.getSignature().toShortString(),
                executionTime,
                status
        );
    }
}