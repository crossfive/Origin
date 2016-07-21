package com.crossfive.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.crossfive.framework.common.enums.State;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    public String value();
    public String description() default "";
    public State state() default State.ACTIVE;
    public int version() default 1;
    
}
