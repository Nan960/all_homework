package com.wjy.ioc;

import com.wjy.ioc.exception.NoSuchBeanDefinitionException;

import javax.annotation.Nonnull;

/**
 * 所有配置的BeanDefinition容器；
 * 后续BeanGenerator创建Bean使用
 *
 * @author wjy
 * @date 2020/9/12
 */
public interface BeanDefinitionRegister {

    /**
     * 注册BeanDefinition
     *
     * @param beanDefinition {@link BeanDefinition}
     */
    void registerBeanDefinition(@Nonnull BeanDefinition beanDefinition);

    /**
     * 删除BeanDefinition
     *
     * @param beanName BeanID {@link BeanDefinition#getBeanName()}
     * @return {@link BeanDefinition#getBeanName()}
     * @throws NoSuchBeanDefinitionException 没有此BeanDefinition
     */
    BeanDefinition removeBeanDefinition(@Nonnull String beanName) throws NoSuchBeanDefinitionException;

    /**
     * 获取指定BeanDefinition
     *
     * @param beanName BeanID {@link BeanDefinition#getBeanName()}
     * @return {@link BeanDefinition}
     * @throws NoSuchBeanDefinitionException 没有此BeanDefinition
     */
    BeanDefinition getBeanDefinition(@Nonnull String beanName) throws NoSuchBeanDefinitionException;

    /**
     * 获取指定BeanDefinition
     *
     * @param beanType BeanID {@link BeanDefinition#getBeanClass()}
     * @return {@link BeanDefinition}
     * @throws NoSuchBeanDefinitionException 没有此BeanDefinition,{@link com.wjy.ioc.exception.IocException} 不能确定唯一
     */
    BeanDefinition getBeanDefinition(@Nonnull Class<?> beanType) throws NoSuchBeanDefinitionException;

    /**
     * 获取所有已经注册的BeanID
     *
     * @return BeanID数组
     */
    String[] getBeanDefinitionNames();

    /**
     * 有多少Bean被管理
     *
     * @return countNumber
     */
    int getBeanDefinitionCount();

    /**
     * BeanID是否使用
     *
     * @param beanName BeanID {@link BeanDefinition#getBeanName()}
     * @return 是否
     */
    boolean isBeanNameInUse(@Nonnull String beanName);

    /**
     * BeanDefinition是否已被管理
     *
     * @param definition BeanID {@link BeanDefinition}
     * @return 是否
     */
    boolean containsDefinition(@Nonnull BeanDefinition definition);

    /**
     * 此类型Bean是否已经注册
     *
     * @param beanType BeanID {@link BeanDefinition#getBeanClass()} ()}
     * @return 是否
     */
    boolean containsDefinition(@Nonnull Class<?> beanType);
}
