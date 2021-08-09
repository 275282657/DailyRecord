package com.cgp.dailyrecord.aop.main;

import com.cgp.dailyrecord.aop.annotation.Annotation1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author chenguopeng
 * @Date 2021/8/9 10:34
 */
@Component
@Slf4j
public class AnnotationDemo {

	@Annotation1(age = -1,name = "cc1",score = {1})
	public void annotationDemo(){
		log.info("start annotationDemo ");
	}


}
