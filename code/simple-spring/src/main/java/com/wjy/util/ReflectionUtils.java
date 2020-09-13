package com.wjy.util;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author wjy
 * @date 2020/9/12
 */
public class ReflectionUtils {

    /**
     * 无参构造创建实例
     *
     * @param beanClass 目标类
     * @param <T>       目标类型
     * @return 目标实例
     */
    public static <T> T createInstance(@Nonnull Class<T> beanClass) {
        final Constructor<T> constructor;
        try {
            constructor = beanClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new ReflectionUtilsException("调用无参构造实例化Bean失败", e);
        }
    }

    public static Class<?>[] getInterfaces(@Nonnull Class<?> tClass) {
        return tClass.getInterfaces();
    }


    static class ReflectionUtilsException extends RuntimeException {

        public ReflectionUtilsException() {
        }

        public ReflectionUtilsException(String message) {
            super(message);
        }

        public ReflectionUtilsException(String message, Throwable cause) {
            super(message, cause);
        }

        public ReflectionUtilsException(Throwable cause) {
            super(cause);
        }

        public ReflectionUtilsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
