package com.wjy.ioc.exception;

import javax.annotation.Nonnull;

/**
 * @author wjy
 * @date 2020/9/11
 */
public class NoSuchBeanException extends IocException {

    private static final String PREFIX = "未找到Bean：";

    public NoSuchBeanException() {
        super();
    }

    public NoSuchBeanException(@Nonnull Class<?> type) {
        super(PREFIX + "Class-" + type.getName());
    }

    public NoSuchBeanException(String message) {
        super(PREFIX + message);
    }

    public NoSuchBeanException(String message, Throwable cause) {
        super(PREFIX + message, cause);
    }

    public NoSuchBeanException(Throwable cause) {
        super(cause);
    }

    protected NoSuchBeanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(PREFIX + message, cause, enableSuppression, writableStackTrace);
    }
}
