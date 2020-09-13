package com.wjy.ioc;

import com.wjy.ioc.demo.Demo2;
import org.junit.Before;
import org.junit.Test;

/**
 * @author wjy
 * @date 2020/9/11
 */
public class BeanFactoryTest {

    private SingletonBeanFactoryImpl beanFactory;

    @Before
    public void before() {
        beanFactory = new SingletonBeanFactoryImpl();
        beanFactory.setBean("id1", new Demo2());
    }

    @Test
    public void testSingletonBeanFactory() {
        System.out.println(beanFactory.getBean("id1"));
        // System.out.println(beanFactory.getBean("id12"));
        System.out.println(beanFactory.getBean("id1", Demo2.class));
    }
}
