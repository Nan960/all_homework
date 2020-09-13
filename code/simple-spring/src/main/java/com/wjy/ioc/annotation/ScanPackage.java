package com.wjy.ioc.annotation;

import java.lang.annotation.*;

/**
 * @author wjy
 * @date 2020/9/13
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ScanPackage {

    /**
     * 指定扫描包路径
     */
    String[] value() default {};
}
