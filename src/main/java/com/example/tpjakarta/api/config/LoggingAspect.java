package com.example.tpjakarta.api.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.example.tpjakarta.services.*.*(..))")
    public Object profileAndLogServiceMethods(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().toShortString();
        Object[] args = pjp.getArgs();

        if (logger.isDebugEnabled()) {
             // Only log length of args to avoid lazy loading issues or exposing PII
            logger.debug("Entering method: [{}] with {} argument(s).", methodName, args.length);
        } else {
             logger.info("Entering method: [{}]", methodName);
        }

        long start = System.currentTimeMillis();
        try {
            Object result = pjp.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            logger.info("Exiting method: [{}] - Execution time: {} ms", methodName, elapsedTime);
            return result;
        } catch (Throwable e) {
            long elapsedTime = System.currentTimeMillis() - start;
            logger.error("Exception in method: [{}] - Execution time before failure: {} ms - Exception Type: {} - Message: {}", 
                    methodName, elapsedTime, e.getClass().getName(), e.getMessage());
            throw e; // Rethrow to not break the application flow
        }
    }
}
