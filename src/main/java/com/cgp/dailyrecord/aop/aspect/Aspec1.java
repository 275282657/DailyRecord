package com.cgp.dailyrecord.aop.aspect;

import com.cgp.dailyrecord.aop.annotation.Annotation1;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author chenguopeng
 * @Date 2021/8/9 10:37
 */
@Component
@Aspect
@Slf4j
public class Aspec1 {
	@Pointcut("@annotation(com.cgp.dailyrecord.aop.annotation.Annotation1)")
	public void aspec1() {
	}

	@Before("aspec1()")
	public void doBefore(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Annotation1 annotation1 = signature.getMethod().getAnnotation(Annotation1.class);
		log.info("I comming");
		log.info(String.format("name:[%s],age[%s],score:[%s]",annotation1.name(),annotation1.age(),annotation1.score()));
	}


	@AfterReturning("aspec1()")
	public void doAfterReturning(JoinPoint joinPoint) {
		doAfter();
	}

	public void doAfter(){
		log.info("exit");
	}
}
