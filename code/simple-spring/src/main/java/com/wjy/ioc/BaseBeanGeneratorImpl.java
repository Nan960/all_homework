package com.wjy.ioc;

import com.wjy.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * 通用Bean创建生命周期管理
 *
 * @author wjy
 * @date 2020/9/11
 */
public class BaseBeanGeneratorImpl implements BeanGenerator {

    private final Logger logger = LoggerFactory.getLogger(BaseBeanGeneratorImpl.class);

    private List<BeanPostProcessor> beanPostProcessorList;

    public BaseBeanGeneratorImpl() {
    }

    @Override
    public Object createBean(BeanDefinition definition) {
        logger.debug("createBean name:{}", definition.getBeanName());

        // 实例化Bean
        Object emptyBean = createBeanInstance(definition);
        // 实例化Bean后调用处理接口
        Object beforeProcessedBean = postBeforeProcess(emptyBean, definition);

        // 注入属性
        Object enrichBean = injectionProperties(beforeProcessedBean, definition);
        // 注入属性后调用后置处理
        Object bean = postAfterProcess(enrichBean, definition);

        logger.debug("createdBean name:{},value:{}", definition.getBeanName(), bean);
        return bean;
    }

    /** 实例化Bean */
    protected Object createBeanInstance(BeanDefinition definition) {
        return ReflectionUtils.createInstance(definition.getBeanClass());
    }

    /** 注入属性 */
    protected Object injectionProperties(Object bean, BeanDefinition definition) {
        return bean;
    }

    /** 依赖注入前处理 */
    protected Object postBeforeProcess(Object bean, BeanDefinition definition) {
        if (Objects.nonNull(beanPostProcessorList)) {
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                bean = beanPostProcessor.postProcessBeanBeforeInjectProperties(bean, definition.getBeanName());
            }
        }
        return bean;
    }

    /** 依赖注入后,Bean初始化最后处理 */
    protected Object postAfterProcess(Object bean, BeanDefinition definition) {
        if (Objects.nonNull(beanPostProcessorList)) {
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                bean = beanPostProcessor.postProcessBeanAfterInjectProperties(bean, definition.getBeanName());
            }
        }
        return bean;
    }

    public List<BeanPostProcessor> getBeanPostProcessorList() {
        return beanPostProcessorList;
    }

    public BaseBeanGeneratorImpl setBeanPostProcessorList(List<BeanPostProcessor> beanPostProcessorList) {
        this.beanPostProcessorList = beanPostProcessorList;
        return this;
    }
}
