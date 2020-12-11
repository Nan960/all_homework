package com.wjy.util.annotation;

import java.lang.annotation.*;

/**
 * 通过工具，可以在注解间传递值
 *
 * @author wjy
 * @date 2020/9/10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface AliasFor {

    /**
     * 目标注解{@link #annotation()} 的具体方法名
     */
    String attribute() default "";

    /**
     * 目标注解
     */
    Class<? extends Annotation> annotation() default Annotation.class;
}
