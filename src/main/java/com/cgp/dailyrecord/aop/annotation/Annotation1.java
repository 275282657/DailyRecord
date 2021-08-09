package com.cgp.dailyrecord.aop.annotation;

import java.lang.annotation.*;

/**
 * @Author chenguopeng
 * @Date 2021/8/9 10:33
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
@Documented
public @interface Annotation1 {
	String name() default "cc";
	int age() default 18;
	int[] score() default {1,2,3};
}
