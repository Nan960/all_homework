package com.wjy.ioc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * IOC管理Bean定义,需要实现equals方法
 *
 * @author wjy
 * @date 2020/9/11
 */
public interface BeanDefinition extends AttributeAccessor {

    /** 单例 */
    String SINGLETON = "singleton";
    /** 原型/多例 */
    String PROTOTYPE = "prototype";


    /**
     * 指定Bean类型
     *
     * @param beanType Bean类型
     * @return
     */
    @Nonnull
    BeanDefinition setBeanClass(@Nonnull Class<?> beanType);

    /**
     * 获取Bean的类型
     *
     * @return Bean类型
     */
    @Nonnull
    Class<?> getBeanClass();

    /**
     * 设置Bean的id
     *
     * @param beanName 指定BeanID
     * @return BeanDefinition
     */
    @Nonnull
    BeanDefinition setBeanName(@Nullable String beanName);

    /**
     * 获取Bean的id
     *
     * @return {@link #setBeanName(String)}的参数 如果null，默认 {@link #getBeanClass()}.getName()
     */
    @Nonnull
    String getBeanName();

    /**
     * 是否单例
     *
     * @return boolean
     */
    boolean isSingleton();

    /**
     * 是否多例
     *
     * @return boolean
     */
    boolean isPrototype();
}
