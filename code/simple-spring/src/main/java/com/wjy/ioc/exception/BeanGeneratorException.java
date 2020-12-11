package com.wjy.ioc.exception;

/**
 * @author wjy
 * @date 2020/9/12
 */
public class BeanGeneratorException extends IocException {
    public BeanGeneratorException() {
    }

    public BeanGeneratorException(String message) {
        super(message);
    }

    public BeanGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanGeneratorException(Throwable cause) {
        super(cause);
    }

    public BeanGeneratorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
