package com.wjy.ioc;

import com.wjy.ioc.exception.BeanGeneratorException;
import com.wjy.ioc.exception.IocException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 单例Bean创建生命周期管理
 *
 * @author wjy
 * @date 2020/9/11
 */
public class SingletonBeanGeneratorImplImpl extends BaseBeanGeneratorImpl {

    private final SingletonBeanFactoryImpl beanFactory = new SingletonBeanFactoryImpl();

    /** 用于解决循环依赖，存放半成品Bean */
    private final Map<String, Object> semiBeanFactory = new HashMap<>();

    public SingletonBeanGeneratorImplImpl() {
    }

    @Override
    public Object createBean(BeanDefinition definition) {
        if (!definition.isSingleton()) {
            throw new BeanGeneratorException("使用 SingletonBeanGeneratorImpl 生产非 Singleton Bean");
        }
        // 单例模式只创建一次Bean 有可能是循环依赖创建的Bean这里不能重复创建
        if (this.beanFactory.containsBean(definition.getBeanName())) {
            return this.beanFactory.getBean(definition.getBeanName());
        }
        return super.createBean(definition);
    }

    @Override
    protected Object injectionProperties(Object bean, BeanDefinition definition) {
        final Class<?> beanClass = definition.getBeanClass();
        for (String attributeName : definition.attributeNames()) {
            Object attribute = definition.getAttribute(attributeName);

            if (attribute instanceof BeanDefinition) {
                // 属性是IOC管理Bean
                if (beanFactory.containsBean(((BeanDefinition) attribute).getBeanName())) {
                    attribute = beanFactory.getBean(((BeanDefinition) attribute).getBeanName());
                } else if (semiBeanFactory.containsKey(((BeanDefinition) attribute).getBeanName())) {
                    // 解决循环依赖
                    attribute = semiBeanFactory.get(((BeanDefinition) attribute).getBeanName());
                } else {
                    // 新建Bean
                    attribute = this.createBean((BeanDefinition) attribute);
                }
            }

            try {
                final Field field = beanClass.getDeclaredField(attributeName);
                field.setAccessible(true);
                field.set(bean, attribute);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new IocException("属性注入失败", e);
            }
        }
        return bean;
    }

    @Override
    protected Object postBeforeProcess(Object bean, BeanDefinition definition) {
        final Object obj = super.postBeforeProcess(bean, definition);
        // 暂存Bean
        this.semiBeanFactory.put(definition.getBeanName(), obj);
        return obj;
    }

    @Override
    protected Object postAfterProcess(Object bean, BeanDefinition definition) {
        bean = super.postAfterProcess(bean, definition);
        // 将实例化的Bean放入IOC容器
        this.beanFactory.setBean(definition.getBeanName(), bean);
        this.semiBeanFactory.remove(definition.getBeanName());
        return bean;
    }

    public SingletonBeanFactoryImpl getBeanFactory() {
        return beanFactory;
    }
}
