package com.wjy.util.annotation;

/**
 * @author wjy
 * @date 2020/9/10
 */
public class AnnotationConfigurationException extends RuntimeException {
    public AnnotationConfigurationException() {
        super();
    }

    public AnnotationConfigurationException(String message) {
        super(message);
    }

    public AnnotationConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnnotationConfigurationException(Throwable cause) {
        super(cause);
    }

    protected AnnotationConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
