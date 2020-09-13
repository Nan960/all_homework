package com.wjy.ioc;

/**
 * Bean实例化顶级接口
 *
 * @author wjy
 * @date 2020/9/11
 */
public interface BeanGenerator {

    /**
     * 创建Bean
     *
     * @param definition Bean配置信息
     * @return 实例化的Bean
     */
    Object createBean(BeanDefinition definition);
}
