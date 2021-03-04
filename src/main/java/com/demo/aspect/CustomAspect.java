package com.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class CustomAspect {

    @After("@annotation(com.demo.aspect.CustomAspect.Read)")
    public void after(JoinPoint point) {
        System.out.println(Arrays.asList(point.getArgs()));
    }

    public @interface Read {
    }

}
