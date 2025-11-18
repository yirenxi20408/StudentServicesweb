package cn.edu.hzcu.yrx.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * 定义切点：StudentService中的所有方法
     */
    @Pointcut("execution(* cn.edu.hzcu.yrx.demo.service.StudentService.*(..))")
    public void studentServiceMethods() {}

    /**
     * 环绕通知：记录方法调用的详细日志
     */
    @Around("studentServiceMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        
        logger.info("==> 调用方法: {}, 参数: {}", methodName, Arrays.toString(args));
        
        long startTime = System.currentTimeMillis();
        Object result = null;
        
        try {
            result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;
            logger.info("<== 方法 {} 执行成功, 耗时: {}ms, 返回值: {}", methodName, elapsedTime, result);
            return result;
        } catch (Exception e) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            logger.error("<== 方法 {} 执行失败, 耗时: {}ms, 异常: {}", methodName, elapsedTime, e.getMessage());
            throw e;
        }
    }

    /**
     * 前置通知：方法调用前
     */
    @Before("studentServiceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.debug(">>> 准备执行方法: {}", methodName);
    }

    /**
     * 后置通知：方法成功返回后
     */
    @AfterReturning(pointcut = "studentServiceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        logger.debug("<<< 方法 {} 正常返回", methodName);
    }

    /**
     * 异常通知：方法抛出异常后
     */
    @AfterThrowing(pointcut = "studentServiceMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        logger.warn("!!! 方法 {} 抛出异常: {}", methodName, exception.getClass().getSimpleName());
    }
}
