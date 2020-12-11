package com.wjy.util.annotation;

import com.wjy.ioc.annotation.Autowired;
import com.wjy.ioc.annotation.Service;

/**
 * @author wjy
 * @date 2020/9/10
 */
@Service("demo")
public class Demo1 {

    private String test;

    @Autowired
    private String test2;

    @Autowired
    public Demo1 setTest(String test) {
        this.test = test;
        return this;
    }
}
