package com.demo.aspect;

import com.qmw.util.ParamsPrinter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
public class GlobalAspect {

    @Resource
    private HttpServletRequest request;

    @Pointcut("execution(* com.demo.controller.*.*(..)) && !execution(* com.demo.controller.IndexPageController.*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object aspect(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1.前置动作
        long start = System.currentTimeMillis();
        if (log.isInfoEnabled())
            ParamsPrinter.print(request);

        // 2.执行方法，并将返回的结果放入ResponseResult中
        Object result = joinPoint.proceed();

        // 3.后置动作
        log.info("[{}][{}]执行用时：{}ms",
                request.getMethod(),
                request.getRequestURI(),
                System.currentTimeMillis() - start);

        return result;
    }

}
