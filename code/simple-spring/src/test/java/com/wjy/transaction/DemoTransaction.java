package com.wjy.transaction;

import com.wjy.ioc.annotation.Bean;

/**
 * @author wjy
 * @date 2020/9/13
 */
public class DemoTransaction {

    private String test;

    @Bean
    @Transaction
    public void test(){

    }

}
