package com.elv.web.aop;

import java.time.Duration;
import java.time.Instant;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.elv.web.constant.Const;
import com.elv.web.model.ApiResult;
import com.elv.web.util.RequestUtil;

/**
 * calculate API Time-consuming
 *
 * @author lxh
 * @since 2020-04-08
 */
@Aspect
@Component
public class CostAspect {

    private static final Logger logger = LoggerFactory.getLogger(CostAspect.class);

    static {
        logger.info("CostAspect#static has run.");
    }

    // @Pointcut("execution (* com.elv..*.controller..*.*(..)) || execution (* com.elv..*.ctrl..*.*(..))")
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PostMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.GetMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PutMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PatchMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.DeleteMapping) ")
    public void beforePointcut() {

    }

    @Before("beforePointcut()")
    public void before(JoinPoint joinPoint) {
        // logger.info("CostAspect#before has run.");
    }

    @Around("execution (* com.elv..*.controller..*.*(..)) || execution (* com.elv..*.ctrl..*.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();

        Instant startInstant = Instant.now();
        Object result = pjp.proceed(args);
        if (!(result instanceof ApiResult)) {
            return result;
        }
        long cost = Duration.between(startInstant, Instant.now()).toMillis(); // unit:milliseconds
        if (cost > Const.THRESHOLD_SLOW_OPT) {
            logger.warn("CostAspect#around, API slow response:uri = {}, cost = {}",
                    RequestUtil.getRequest().getRequestURI(), cost);
        }

        ApiResult apiResult = (ApiResult) result;
        apiResult.setCost(cost);
        return apiResult;
    }
}
