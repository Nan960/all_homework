package com.wjy.demo;

import com.wjy.ioc.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author wjy
 * @date 2020/9/13
 */
public class Demo1 extends BaseBeanFactoryImpl implements BeanPostProcessor, BeanFactory, BeanDefinition, DemoInterface1 {
    public Demo1(BeanFactory singletonBeanFactory, BeanGenerator baseBeanGenerator, BeanDefinitionRegister globalBeanDefinitionRegister) {
        super(singletonBeanFactory, baseBeanGenerator, globalBeanDefinitionRegister);
    }

    @Nonnull
    @Override
    public BeanDefinition setBeanClass(@Nonnull Class<?> beanType) {
        return null;
    }

    @Nonnull
    @Override
    public Class<?> getBeanClass() {
        return null;
    }

    @Nonnull
    @Override
    public BeanDefinition setBeanName(@Nullable String beanName) {
        return null;
    }

    @Nonnull
    @Override
    public String getBeanName() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public boolean isPrototype() {
        return false;
    }

    @Override
    public void setAttribute(String name, @Nullable Object value) {

    }

    @Nullable
    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Nullable
    @Override
    public Object removeAttribute(String name) {
        return null;
    }

    @Override
    public boolean hasAttribute(String name) {
        return false;
    }

    @Override
    public String[] attributeNames() {
        return new String[0];
    }

}
