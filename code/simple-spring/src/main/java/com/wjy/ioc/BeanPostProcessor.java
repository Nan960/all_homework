package com.wjy.ioc;

/**
 * IOC容器会在实例化Bean后，最后调用此处理器处理Bean
 *
 * @author wjy
 * @date 2020/9/10
 */
public interface BeanPostProcessor {

    /**
     * 此方法会在Bean实例化后，未注入属性前调用
     *
     * @param bean     实例化的Bean
     * @param beanName BeanID
     * @return Object
     */
    default Object postProcessBeanBeforeInjectProperties(Object bean, String beanName) {
        return bean;
    }

    /**
     * 此方法会在Bean注入属性后调用</br>
     * 只能在原有对象基础上操作
     *
     * @param bean     初始化后的Bean
     * @param beanName BeanID
     * @return Object
     */
    default Object postProcessBeanAfterInjectProperties(Object bean, String beanName) {
        return bean;
    }
}
