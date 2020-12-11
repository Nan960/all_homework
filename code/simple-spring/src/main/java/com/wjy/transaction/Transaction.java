package com.wjy.transaction;

import java.lang.annotation.*;

/**
 * @author wjy
 * @date 2020/9/13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Transaction {
}
