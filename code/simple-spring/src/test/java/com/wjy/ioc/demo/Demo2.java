package com.wjy.ioc.demo;

import com.wjy.ioc.annotation.Autowired;
import com.wjy.ioc.annotation.Service;

/**
 * @author wjy
 * @date 2020/9/10
 */
@Service("demo")
public class Demo2 {

    private String test;

    @Autowired
    private Demo1 demo1;

    @Autowired
    public Demo2 setTest(String test) {
        this.test = test;
        return this;
    }

    public Demo2 setDemo1(Demo1 demo1) {
        this.demo1 = demo1;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Demo2{");
        sb.append("test='").append(test).append('\'');
        sb.append(", demo1=").append(demo1);
        sb.append('}');
        return sb.toString();
    }
}
