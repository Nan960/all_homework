package com.wjy.ioc.exception;

/**
 * @author wjy
 * @date 2020/9/12
 */
public class NoSuchBeanDefinitionException extends IocException {

    private static final String PREFIX = "未找到BeanDefinition：";

    public NoSuchBeanDefinitionException() {
    }

    public NoSuchBeanDefinitionException(Class<?> type) {
        super(PREFIX + type.getName());
    }

    public NoSuchBeanDefinitionException(String message) {
        super(PREFIX + message);
    }

    public NoSuchBeanDefinitionException(String message, Throwable cause) {
        super(PREFIX + message, cause);
    }

    public NoSuchBeanDefinitionException(Throwable cause) {
        super(cause);
    }

    public NoSuchBeanDefinitionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(PREFIX + message, cause, enableSuppression, writableStackTrace);
    }
}
