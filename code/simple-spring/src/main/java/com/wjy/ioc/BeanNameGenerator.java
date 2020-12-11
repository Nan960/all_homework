package com.wjy.ioc;

import com.wjy.util.StringUtils;

/**
 * BeanId生成器，在生成BeanDefinition之前调用
 *
 * @author wjy
 * @date 2020/9/12
 */
public interface BeanNameGenerator {

    /**
     * 通用BeanID修改器
     *
     * @param originalName 默认/指定 ID
     * @param beanType     BeanType
     * @return 新BeanID
     */
    default String generateName(String originalName, Class<?> beanType) {
        if (StringUtils.isEmpty(originalName)) {
            return beanType.getName();
        }
        return originalName;
    }
}
