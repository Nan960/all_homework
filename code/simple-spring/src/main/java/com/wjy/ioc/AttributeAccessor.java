package com.wjy.ioc;

import javax.annotation.Nullable;

/**
 * Bean属性存储器
 *
 * @author wjy
 * @date 2020/9/11
 */
public interface AttributeAccessor {

    /**
     * 设置属性
     *
     * @param name  属性名
     * @param value 属性值；如果是{@link BeanDefinition}为IOC管理Bean，否则为已经实例化好的Object
     */
    void setAttribute(String name, @Nullable Object value);

    @Nullable
    Object getAttribute(String name);

    @Nullable
    Object removeAttribute(String name);

    boolean hasAttribute(String name);

    String[] attributeNames();
}
