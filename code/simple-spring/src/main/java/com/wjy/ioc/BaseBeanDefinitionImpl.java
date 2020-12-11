package com.wjy.ioc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author wjy
 * @date 2020/9/12
 */
public class BaseBeanDefinitionImpl implements BeanDefinition {

    private String beanName;
    private Class<?> beanClass;
    private final String scope;
    private final boolean singleton;
    private final boolean prototype;

    private final Map<String, Object> attributes = new HashMap<>(128);

    public BaseBeanDefinitionImpl(@Nonnull String scope) {
        this.scope = scope;
        this.singleton = scope.trim().toLowerCase(Locale.ENGLISH).equals(SINGLETON);
        this.prototype = scope.trim().toLowerCase(Locale.ENGLISH).equals(PROTOTYPE);
    }

    @Nonnull
    @Override
    public BeanDefinition setBeanClass(@Nonnull Class<?> beanType) {
        this.beanClass = beanType;
        return this;
    }

    @Nonnull
    @Override
    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    @Nonnull
    @Override
    public BeanDefinition setBeanName(@Nullable String beanName) {
        this.beanName = beanName;
        return this;
    }

    @Nonnull
    @Override
    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public boolean isSingleton() {
        return this.singleton;
    }

    @Override
    public boolean isPrototype() {
        return this.prototype;
    }

    @Override
    public void setAttribute(String name, @Nullable Object value) {
        attributes.put(name, value);
    }

    @Nullable
    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Nullable
    @Override
    public Object removeAttribute(String name) {
        return attributes.remove(name);
    }

    @Override
    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    @Override
    public String[] attributeNames() {
        return attributes.keySet().toArray(new String[attributes.size()]);
    }
}
