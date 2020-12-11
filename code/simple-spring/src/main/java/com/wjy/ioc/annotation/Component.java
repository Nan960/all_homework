package com.wjy.ioc.annotation;

import java.lang.annotation.*;

/**
 * 被此注解标识的类会被IOC容器管理
 *
 * @author wjy
 * @date 2020/9/10
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

    /**
     * 使用此值作为 {@link com.wjy.ioc.BeanFactory#getBean(String)} 的参数即可获得对应Bean实例</br>
     * 默认值为被修饰类的全限定类名
     */
    String value() default "";

    /**
     * 指定Bean作用域 singleton，prototype
     */
    String scope() default "singleton";

}
