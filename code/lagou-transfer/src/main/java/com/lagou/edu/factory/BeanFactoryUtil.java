package com.lagou.edu.factory;

import com.lagou.edu.SimpleSpringConfig;
import com.wjy.ioc.BeanFactory;
import com.wjy.ioc.config.AnnotationScanPackageConfig;

public class BeanFactoryUtil {

    private static final BeanFactory BEAN_FACTORY;

    static {
        BEAN_FACTORY = new AnnotationScanPackageConfig().start(SimpleSpringConfig.class);
    }

    public static BeanFactory getBeanFactory() {
        return BEAN_FACTORY;
    }

}
