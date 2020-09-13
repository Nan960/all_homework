package com.wjy.ioc;

import com.wjy.ioc.exception.IocException;
import com.wjy.ioc.exception.NoSuchBeanException;

import javax.annotation.Nonnull;

/**
 * 基础Bean工厂实现，支持单例，多例管理
 *
 * @author wjy
 * @date 2020/9/12
 */
public class BaseBeanFactoryImpl implements BeanFactory {

    private final BeanFactory singletonBeanFactory;

    private final BeanGenerator baseBeanGenerator;

    private final BeanDefinitionRegister globalBeanDefinitionRegister;

    public BaseBeanFactoryImpl(BeanFactory singletonBeanFactory, BeanGenerator baseBeanGenerator, BeanDefinitionRegister globalBeanDefinitionRegister) {
        this.singletonBeanFactory = singletonBeanFactory;
        this.baseBeanGenerator = baseBeanGenerator;
        this.globalBeanDefinitionRegister = globalBeanDefinitionRegister;
    }

    @Override
    public Object getBean(@Nonnull String name) throws NoSuchBeanException {
        return getBean(globalBeanDefinitionRegister.getBeanDefinition(name));
    }

    @Override
    public <T> T getBean(@Nonnull Class<T> beanType) throws IocException {
        return (T) getBean(globalBeanDefinitionRegister.getBeanDefinition(beanType));
    }

    @Override
    public <T> T getBean(@Nonnull String name, @Nonnull Class<T> beanType) throws NoSuchBeanException {
        final BeanDefinition definition = globalBeanDefinitionRegister.getBeanDefinition(name);
        final BeanDefinition definition1 = globalBeanDefinitionRegister.getBeanDefinition(beanType);
        if (!definition.equals(definition1)) {
            throw new IocException("name获取BeanDefinition的类型和banType不匹配 name:" + name + ",value:" + beanType.getName());
        }
        return getBean(beanType);
    }

    private Object getBean(@Nonnull BeanDefinition beanDefinition) throws NoSuchBeanException {
        if (beanDefinition.isSingleton()) {
            return singletonBeanFactory.getBean(beanDefinition.getBeanName());
        } else {
            return baseBeanGenerator.createBean(beanDefinition);
        }
    }

    @Override
    public Class<?> getType(@Nonnull String name) throws NoSuchBeanException {
        return globalBeanDefinitionRegister.getBeanDefinition(name).getBeanClass();
    }

    @Override
    public Object removeBean(@Nonnull String name) {
        final BeanDefinition beanDefinition = globalBeanDefinitionRegister.removeBeanDefinition(name);
        if (beanDefinition.isSingleton()) {
            return singletonBeanFactory.removeBean(name);
        } else {
            return null;
        }
    }

    @Override
    public boolean containsBean(@Nonnull String name) {
        return globalBeanDefinitionRegister.isBeanNameInUse(name);
    }

    @Override
    public <T> boolean containsBean(@Nonnull Class<T> beanType) {
        return globalBeanDefinitionRegister.containsDefinition(beanType);
    }

    @Override
    public boolean isTypeMatch(@Nonnull String name, @Nonnull Class<?> typeToMatch) {
        final BeanDefinition definition = globalBeanDefinitionRegister.getBeanDefinition(name);
        return definition.getBeanClass().equals(typeToMatch);
    }

    public BeanFactory getSingletonBeanFactory() {
        return singletonBeanFactory;
    }

    public BeanGenerator getBaseBeanGenerator() {
        return baseBeanGenerator;
    }

    public BeanDefinitionRegister getGlobalBeanDefinitionRegister() {
        return globalBeanDefinitionRegister;
    }
}
