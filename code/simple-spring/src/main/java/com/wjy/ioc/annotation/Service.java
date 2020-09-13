package com.wjy.ioc.annotation;

import com.wjy.util.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识业务层Bean
 *
 * @author wjy
 * @date 2020/9/10
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Service {

    @AliasFor(annotation = Component.class, attribute = "value")
    String value() default "";

}
