package com.wjy.ioc.annotation;

import java.lang.annotation.*;

/**
 * 加载属性或者方法上，指定注入对象
 *
 * @author wjy
 * @date 2020/9/10
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

    /**
     * 是否必须
     * true：根据类型注入，找不到则报错
     * false：注入属性为null
     */
    boolean required() default true;

    /**
     * 注入属性在IOC中管理的BeanID，不填写，则根据类型注入
     */
    String value() default "";

}
