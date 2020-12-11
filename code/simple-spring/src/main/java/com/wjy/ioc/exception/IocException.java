package com.wjy.ioc.exception;

/**
 * @author wjy
 * @date 2020/9/10
 */
public class IocException extends RuntimeException {
    public IocException() {
        super();
    }

    public IocException(String message) {
        super(message);
    }

    public IocException(String message, Throwable cause) {
        super(message, cause);
    }

    public IocException(Throwable cause) {
        super(cause);
    }

    protected IocException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
