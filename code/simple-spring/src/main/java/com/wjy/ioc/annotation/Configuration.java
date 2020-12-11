package com.wjy.ioc.annotation;

import com.wjy.util.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wjy
 * @date 2020/9/12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Configuration {

    @AliasFor(annotation = Component.class, attribute = "value")
    String value() default "";
}
