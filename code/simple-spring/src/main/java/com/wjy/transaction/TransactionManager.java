package com.wjy.transaction;

import com.wjy.ioc.BeanPostProcessor;
import com.wjy.util.annotation.AnnotationUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 声明式事务管理器
 * 利用后置处理器，全局拦截IOC 管理Bean，判断是否开启事务， BeanPostProcessor
 *
 * @author wjy
 * @date 2020/9/13
 */
public interface TransactionManager extends BeanPostProcessor {
    Logger logger = LoggerFactory.getLogger(TransactionManager.class);

    @Override
    default Object postProcessBeanAfterInjectProperties(Object bean, String beanName) {
        // TODO 实现原因，只实现方法层级注解解析，未实现类级别
        // 注意这里没有区别service，repository，因为考虑到可能任意类都可以用，所以没做限制

        List<String> needProxyMethods = new ArrayList<>();
        final Class<?> beanClass = bean.getClass();
        // 获取所有需要代理的方法
        for (Method method : beanClass.getMethods()) {
            final Transaction transaction = AnnotationUtils.findAnnotation(method, Transaction.class);
            if (Objects.nonNull(transaction)) {
                needProxyMethods.add(method.getName());
            }
        }
        if (needProxyMethods.size() > 0) {
            // 如果有需要拦截方法，建代理类
            final Class<?>[] interfaces = beanClass.getInterfaces();
            if (interfaces.length > 0) {
                // 根据类是否实现接口，判断实现代理方式
                logger.debug("使用JDK动态代理创建事务代理：{}", beanClass.getName());
                return Proxy.newProxyInstance(beanClass.getClassLoader(), interfaces, (proxy, method, args) -> {
                    if (needProxyMethods.contains(method.getName())) {
                        try {
                            start();
                            final Object result = method.invoke(bean, args);
                            commit();
                            return result;
                        } catch (Throwable t) {
                            rollback();
                            throw t;
                        }
                    }
                    return method.invoke(bean, args);
                });
            } else {
                logger.debug("使用CGLIB动态代理创建事务代理：{}", beanClass.getName());
                return Enhancer.create(beanClass, (MethodInterceptor) (obj, method, args, proxy) -> {
                    if (needProxyMethods.contains(method.getName())) {
                        try {
                            start();
                            final Object result = proxy.invoke(bean, args);
                            commit();
                            return result;
                        } catch (Throwable t) {
                            rollback();
                            throw t;
                        }
                    }
                    return proxy.invoke(bean, args);
                });
            }
        } else {
            return bean;
        }
    }

    /**
     * 开始事务
     */
    void start() throws SQLException;

    /**
     * 提交事务
     */
    void commit() throws SQLException;

    /**
     * 回滚事务
     */
    void rollback() throws SQLException;

}
