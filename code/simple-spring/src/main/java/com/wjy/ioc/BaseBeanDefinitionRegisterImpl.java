package com.wjy.ioc;

import com.wjy.ioc.exception.IocException;
import com.wjy.ioc.exception.NoSuchBeanDefinitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wjy
 * @date 2020/9/12
 */
public class BaseBeanDefinitionRegisterImpl implements BeanDefinitionRegister {

    private Logger logger = LoggerFactory.getLogger(BaseBeanDefinitionRegisterImpl.class);

    /** Definition容器 */
    final private ConcurrentHashMap<String, BeanDefinition> beanDefinitionConcurrentHashMap = new ConcurrentHashMap<>(128);
    /** 类型名称和BeanID映射 */
    private final ConcurrentHashMap<String, Set<String>> typeIdConcurrentHashMap = new ConcurrentHashMap<>(128);

    @Override
    public void registerBeanDefinition(@Nonnull BeanDefinition beanDefinition) {
        final String beanName = beanDefinition.getBeanName();
        // 存储Definition
        beanDefinitionConcurrentHashMap.put(beanName, beanDefinition);
        // 添加类型映射关系
        final Class<?> beanClass = beanDefinition.getBeanClass();
        putTypeIdConcurrentHashMap(beanName, beanClass.getName());
        // 添加所有接口类型映射
        for (Class<?> beanClassInterface : beanClass.getInterfaces()) {
            putTypeIdConcurrentHashMap(beanName, beanClassInterface.getName());
        }
        // 添加父类类型映射
        final Class<?> superclass = beanClass.getSuperclass();
        if (!Object.class.equals(superclass)) {
            putTypeIdConcurrentHashMap(beanName, superclass.getName());
        }
        logger.debug("注册BeanDefinition name:{},value:{}", beanName, beanDefinition);
    }

    private void putTypeIdConcurrentHashMap(String beanName, String beanClassName) {
        if (typeIdConcurrentHashMap.containsKey(beanClassName)) {
            typeIdConcurrentHashMap.get(beanClassName).add(beanName);
        } else {
            Set<String> set = new HashSet<>();
            set.add(beanName);
            typeIdConcurrentHashMap.put(beanClassName, set);
        }
    }

    private void removeTypeIdConcurrentHashMap(String name, String className) {
        final Set<String> names = typeIdConcurrentHashMap.get(className);
        names.remove(name);
        if (names.size() == 0) {
            typeIdConcurrentHashMap.remove(className);
        }
    }

    @Override
    public BeanDefinition removeBeanDefinition(@Nonnull String beanName) throws NoSuchBeanDefinitionException {
        final BeanDefinition definition = beanDefinitionConcurrentHashMap.remove(beanName);
        if (Objects.nonNull(definition)) {
            final Class<?> beanClass = definition.getBeanClass();
            // 删除类型映射关系
            removeTypeIdConcurrentHashMap(beanName, beanClass.getName());
            // 删除所有接口类型映射
            for (Class<?> beanClassInterface : beanClass.getInterfaces()) {
                removeTypeIdConcurrentHashMap(beanName, beanClassInterface.getName());
            }
            // 删除父类类型映射
            final Class<?> superclass = beanClass.getSuperclass();
            if (!Object.class.equals(superclass)) {
                removeTypeIdConcurrentHashMap(beanName, superclass.getName());
            }
        }
        return definition;
    }

    @Override
    public BeanDefinition getBeanDefinition(@Nonnull String beanName) throws NoSuchBeanDefinitionException {
        return Optional.ofNullable(beanDefinitionConcurrentHashMap.get(beanName)).orElseThrow(() -> {
            throw new NoSuchBeanDefinitionException(beanName);
        });
    }

    @Override
    public BeanDefinition getBeanDefinition(@Nonnull Class<?> beanType) throws NoSuchBeanDefinitionException {
        final Set<String> set = typeIdConcurrentHashMap.get(beanType.getName());
        if (Objects.isNull(set) || set.size() == 0) {
            throw new NoSuchBeanDefinitionException(beanType);
        } else if (set.size() > 1) {
            throw new IocException("不能确定唯一BeanDefinition：" + beanType.getName());
        } else {
            return getBeanDefinition(set.iterator().next());
        }
    }

    @Override
    public String[] getBeanDefinitionNames() {
        final String[] strings = new String[getBeanDefinitionCount()];
        final Enumeration<String> keys = beanDefinitionConcurrentHashMap.keys();
        int index = 0;
        while (keys.hasMoreElements()) {
            strings[index] = keys.nextElement();
            index++;
        }
        return strings;
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitionConcurrentHashMap.size();
    }

    @Override
    public boolean isBeanNameInUse(@Nonnull String beanName) {
        return beanDefinitionConcurrentHashMap.containsKey(beanName);
    }

    @Override
    public boolean containsDefinition(@Nonnull BeanDefinition definition) {
        return beanDefinitionConcurrentHashMap.containsValue(definition);
    }

    @Override
    public boolean containsDefinition(@Nonnull Class<?> beanType) {
        return typeIdConcurrentHashMap.containsKey(beanType.getName());
    }
}
