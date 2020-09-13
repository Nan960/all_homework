package com.wjy.ioc;

import com.wjy.ioc.exception.IocException;
import com.wjy.ioc.exception.NoSuchBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例Bean容器
 *
 * @author wjy
 * @date 2020/9/11
 */
public class SingletonBeanFactoryImpl implements BeanFactory {

    private Logger logger = LoggerFactory.getLogger(SingletonBeanFactoryImpl.class);

    /** 单例池 */
    final private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>(128);
    /** 类型名称和单例BeanID映射 */
    private final ConcurrentHashMap<String, Set<String>> typeSingletonIdsMap = new ConcurrentHashMap<>(128);

    @Override
    public Object getBean(@Nonnull String name) throws NoSuchBeanException {
        logger.debug("getBean:{}", name);
        return Optional.ofNullable(singletonObjects.get(name)).orElseThrow(() -> {
            throw new NoSuchBeanException(name);
        });
    }

    @Override
    public <T> T getBean(@Nonnull Class<T> beanType) throws IocException {
        final Set<String> beanIds = typeSingletonIdsMap.get(beanType.getName());
        if (Objects.isNull(beanIds) || beanIds.size() == 0) {
            throw new NoSuchBeanException(beanType);
        } else if (beanIds.size() > 1) {
            throw new IocException("不能确定唯一Bean：" + beanType.getName());
        } else {
            final String name = beanIds.iterator().next();
            final Object obj = getBean(name);
            if (beanType.isInstance(obj)) {
                return (T) obj;
            } else {
                throw new NoSuchBeanException(beanType);
            }

        }
    }

    @Override
    public <T> T getBean(@Nonnull String name, @Nonnull Class<T> beanType) throws NoSuchBeanException {
        final Object obj = getBean(name);
        if (beanType.isInstance(obj)) {
            return (T) obj;
        } else {
            throw new IocException("根据id获取实例类型与beanType不匹配;name:" + name + "type:" + beanType.getName());
        }
    }

    @Override
    public Class<?> getType(@Nonnull String name) throws NoSuchBeanException {
        return getBean(name).getClass();
    }

    /**
     * 如果name重复会覆盖
     *
     * @param name BeanID
     * @param bean Bean
     */
    public void setBean(@Nonnull String name, @Nonnull Object bean) {
        // 存储Bean
        singletonObjects.put(name, bean);
        // 添加类型映射关系
        final Class<?> beanClass = bean.getClass();
        putTypeSingletonIdsMap(name, beanClass.getName());
        // 添加所有接口类型映射
        for (Class<?> beanClassInterface : beanClass.getInterfaces()) {
            putTypeSingletonIdsMap(name, beanClassInterface.getName());
        }
        // 添加父类类型映射
        final Class<?> superclass = beanClass.getSuperclass();
        if (!Object.class.equals(superclass)) {
            putTypeSingletonIdsMap(name, superclass.getName());
        }
        logger.debug("setBean name:{},value:{}", name, bean);
    }

    private void putTypeSingletonIdsMap(@Nonnull String name, @Nonnull String className) {
        if (typeSingletonIdsMap.containsKey(className)) {
            typeSingletonIdsMap.get(className).add(name);
        } else {
            Set<String> set = new HashSet<>();
            set.add(name);
            typeSingletonIdsMap.put(className, set);
        }
    }

    private void removeTypeSingletonIdsMap(@Nonnull String name, @Nonnull String className) {
        final Set<String> names = typeSingletonIdsMap.get(className);
        names.remove(name);
        if (names.size() == 0) {
            typeSingletonIdsMap.remove(className);
        }
    }

    @Override
    public Object removeBean(@Nonnull String name) {
        final Object bean = singletonObjects.remove(name);
        if (Objects.nonNull(bean)) {
            final Class<?> beanClass = bean.getClass();
            // 删除类型映射关系
            removeTypeSingletonIdsMap(name, beanClass.getName());
            // 删除所有接口类型映射
            for (Class<?> beanClassInterface : beanClass.getInterfaces()) {
                removeTypeSingletonIdsMap(name, beanClassInterface.getName());
            }
            // 删除父类类型映射
            final Class<?> superclass = beanClass.getSuperclass();
            if (!Object.class.equals(superclass)) {
                removeTypeSingletonIdsMap(name, superclass.getName());
            }
        }
        logger.debug("removeBean name:{}", name);
        return bean;
    }

    @Override
    public boolean containsBean(@Nonnull String name) {
        return singletonObjects.containsKey(name);
    }

    @Override
    public <T> boolean containsBean(Class<T> beanType) {
        return typeSingletonIdsMap.containsKey(beanType.getName());
    }

    @Override
    public boolean isTypeMatch(@Nonnull String name, @Nonnull Class<?> typeToMatch) {
        return Optional.ofNullable(singletonObjects.get(name)).orElseThrow(() -> {
            throw new NoSuchBeanException(name);
        }).getClass().equals(typeToMatch);
    }
}
