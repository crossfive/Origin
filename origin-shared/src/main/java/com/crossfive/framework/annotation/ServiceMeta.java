package com.crossfive.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceMeta {
	public boolean syn() default false;
	public int timeout() default 1000;
	public int retry() default 1;
}
