package com.innowise.WinterProject.logging.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    @Pointcut("within(com.innowise.WinterProject.service.*)")
    public void stringProcessingMethods() {
    }


    @Around("stringProcessingMethods()")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();

        String args = " ";
        try {
            List<String> argsL = Arrays.stream(joinPoint.getArgs()).map(item -> item.toString() + " ").toList();
            args = argsL.toString();
        } catch (Exception e) {
        }

        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        logger.log(Level.INFO, "\n" + methodName + "\nargs: " + args + "\nprocesing time: " + executionTime + " ms");
        return proceed;
    }
}
