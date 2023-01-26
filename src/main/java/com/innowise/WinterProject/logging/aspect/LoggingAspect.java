package com.innowise.WinterProject.logging.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Log4j2
@Aspect
@Component
public class LoggingAspect {

    @Around("within(com.innowise.WinterProject.service.*)")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();

        StringBuffer line = new StringBuffer();
        line.append("\n").append(methodName).append("\nargs: ");
        try {
            line.append(Arrays.stream(joinPoint.getArgs()).map(Object::toString).toList());
        } catch (Exception e) {
        }
        log.info( line.toString());

        StopWatch stopWatch =new StopWatch();
        stopWatch.start();
        Object proceed = joinPoint.proceed();
        stopWatch.stop();

        line.setLength(0);
        line.append("\nprocesing time: ").append(stopWatch.getTotalTimeMillis()).append("ms");

        log.info( line.toString());
        return proceed;
    }
}
