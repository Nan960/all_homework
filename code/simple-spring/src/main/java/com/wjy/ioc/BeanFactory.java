package com.wjy.ioc;

import com.wjy.ioc.exception.IocException;
import com.wjy.ioc.exception.NoSuchBeanException;

import javax.annotation.Nonnull;

/**
 * IOC容器接口
 *
 * @author wjy
 * @date 2020/9/9
 */
public interface BeanFactory {

    /**
     * 获取容器中Bean
     *
     * @param name BeanID
     * @return Object
     * @throws NoSuchBeanException
     */
    Object getBean(@Nonnull String name) throws NoSuchBeanException;

    /**
     * 获取容器中Bean
     *
     * @param beanType BeanType 父类或接口或实现类 类型
     * @param <T>      返回Bean类型
     * @return T
     * @throws IocException 如果获取多个Bean会抛出 {@link IocException} 如果没有此Bean抛出 {@link NoSuchBeanException}
     */
    <T> T getBean(@Nonnull Class<T> beanType) throws IocException;

    /**
     * 与{@link #getBean(String)}作用一样
     *
     * @param name     BeanID
     * @param beanType BeanType
     * @param <T>      返回Bean类型
     * @return T
     * @throws NoSuchBeanException
     */
    <T> T getBean(@Nonnull String name, @Nonnull Class<T> beanType) throws NoSuchBeanException;

    /**
     * 获取Bean类型
     *
     * @param name BeanID
     * @return Class
     * @throws NoSuchBeanException
     */
    Class<?> getType(@Nonnull String name) throws NoSuchBeanException;

    /**
     * 删除IOC容器中的Bean
     *
     * @param name BeanID
     * @return 如果容器中没有此Bean，返回null；否则返回移出对象
     */
    Object removeBean(@Nonnull String name);

    /**
     * 此Bean是否被IOC容器管理
     *
     * @param name BeanID
     * @return true：IOC容器中有此Bean </br>false：IOC容器中没有此Bean
     */
    boolean containsBean(@Nonnull String name);

    /**
     * 此类型Bean是否被IOC容器管理
     *
     * @param beanType BeanType
     * @param <T>      BeanType
     * @return true：IOC容器中有此Bean </br>false：IOC容器中没有此Bean
     */
    <T> boolean containsBean(@Nonnull Class<T> beanType);

    /**
     * 判断 name 指向的Bean类型是否是 typeToMatch
     *
     * @param name        BeanID
     * @param typeToMatch BeanType
     * @return true：类型匹配 </br>false：类型不匹配
     */
    boolean isTypeMatch(@Nonnull String name, @Nonnull Class<?> typeToMatch);

}
