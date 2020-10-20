package com.elv.core.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.elv.core.tool.db.redis.BaseRedis;
import com.elv.frame.exception.RedisException;

/**
 * @author lxh
 * @since 2020-08-25
 */
@Aspect
@Component
public class RedisAspect {

    private static final Logger logger = LoggerFactory.getLogger(RedisAspect.class);

    static {
        logger.info("Annotation @Redis has run. ");
    }

    @Pointcut("@annotation(com.elv.core.annotation.redis.Redis)")
    public void pointcut() {

    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        if (target instanceof BaseRedis) {
            BaseRedis baseRedis = (BaseRedis) target;
            baseRedis.conn();
        }
    }

    @AfterReturning("pointcut()")
    public void afterReturning(JoinPoint joinPoint) {
        this.close(joinPoint);
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        this.close(joinPoint);
        throw new RedisException("Redis access error, " + e.getMessage(), e);
    }

    private void close(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        if (target instanceof BaseRedis) {
            BaseRedis baseRedis = (BaseRedis) target;
            baseRedis.close();
        }
    }
}
