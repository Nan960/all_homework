package com.wjy.ioc;

/**
 * 可以直接放入IOC容器管理
 *
 * @author wjy
 * @date 2020/9/13
 */
public interface FactoryBean {

    /**
     * 创建复杂Bean方法
     *
     * @return Bean实例
     */
    Object createBean();
}
