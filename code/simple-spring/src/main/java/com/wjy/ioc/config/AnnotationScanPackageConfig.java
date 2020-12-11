package com.wjy.ioc.config;

import com.wjy.ioc.*;
import com.wjy.ioc.annotation.Autowired;
import com.wjy.ioc.annotation.Component;
import com.wjy.ioc.annotation.Configuration;
import com.wjy.ioc.annotation.ScanPackage;
import com.wjy.ioc.exception.IocException;
import com.wjy.util.FileScanner;
import com.wjy.util.StringUtils;
import com.wjy.util.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author wjy
 * @date 2020/9/11
 */

public class AnnotationScanPackageConfig {

    /** 单例Bean生成器 */
    private final SingletonBeanGeneratorImplImpl singletonBeanGeneratorImpl = new SingletonBeanGeneratorImplImpl();
    /** 单例工厂 */
    private final SingletonBeanFactoryImpl singletonBeanFactory = singletonBeanGeneratorImpl.getBeanFactory();
    /** 全局BeanDefinition注册器 */
    private final BeanDefinitionRegister beanDefinitionRegister = new BaseBeanDefinitionRegisterImpl();

    /** BeanNameGenerator */
    private BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();
    /** 后置处理器 */
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();
    /** 配置类 */
    private List<BeanDefinition> configBeanDefinition = new ArrayList<>();

    /**
     * 启动接口
     */
    public BeanFactory start(Class<?> configClass) {

        // 1.加载配置类
        String[] scanPackages = parseConfig(configClass);
        if (Objects.isNull(scanPackages) || scanPackages.length == 0) {
            throw new IocException("必须使用 @ScanPackage 指定扫描的包");
        }

        // 2. 扫描包获取所有包路径下类
        final FileScanner fileScanner = new FileScanner();
        final Set<Class<?>> classSet = new HashSet<>();
        for (String path : scanPackages) {
            classSet.addAll(fileScanner.search(path));
        }

        // 3. 生成BeanDefinition
        generateBeanDefinition(classSet);


        // 4. 先实例化配置Bean
        for (BeanDefinition definition : configBeanDefinition) {
            final Object bean = singletonBeanGeneratorImpl.createBean(definition);
            if (bean instanceof BeanPostProcessor) {
                // 如果是后置处理器，添加到后置处理器集合中
                beanPostProcessorList.add((BeanPostProcessor) bean);
            }
        }

        // 添加配置的后置处理器
        singletonBeanGeneratorImpl.setBeanPostProcessorList(beanPostProcessorList);
        // 5. 实例化单例Bean
        for (String definitionName : beanDefinitionRegister.getBeanDefinitionNames()) {
            singletonBeanGeneratorImpl.createBean(beanDefinitionRegister.getBeanDefinition(definitionName));
        }

        // 6. 返回BeanFactory
        return new BaseBeanFactoryImpl(singletonBeanFactory, new BaseBeanGeneratorImpl(), beanDefinitionRegister);
    }

    private String[] parseConfig(Class<?> configClass) {
        Set<String> scanPackage = new HashSet<>();

        final Configuration configuration = AnnotationUtils.findAnnotation(configClass, Configuration.class);
        if (Objects.isNull(configuration)) {
            throw new IocException(configClass.getName() + " 必须是 @Configuration 注解的类");
        }
        final ScanPackage spa = AnnotationUtils.findAnnotation(configClass, ScanPackage.class);
        if (Objects.nonNull(spa)) {
            scanPackage.addAll(Arrays.asList(spa.value()));
        }

        // TODO 添加自定义 BeanNameGenerator
        // this.beanNameGenerator=


        // TODO 判断方法是否被 @Bean注解，如果是则返回值类型也需要IOC管理
        // for (Method method : beanClass.getMethods()) {
        //     final Bean beanAnnotation = AnnotationUtils.findAnnotation(method, Bean.class);
        //     if (Objects.nonNull(beanAnnotation)) {
        //         singletonBeanFactory.setBean(beanNameGenerator.generateName(beanAnnotation.value(), method.getReturnType()), new BeanGenerator() {
        //             @Override
        //             public Object createBean(BeanDefinition definition) {
        //                 return null;
        //             }
        //         });
        //     }
        // }

        return scanPackage.toArray(new String[scanPackage.size()]);
    }

    private void generateBeanDefinition(Set<Class<?>> classSet) {
        // 遍历，过滤出被@Component注解的类 创建BeanDefinition
        for (Class<?> aClass : classSet) {
            // 创建并注册 DeanDefinition
            registerBeanDefinition(aClass);
        }

        // BeanDefinition批量设置属性
        for (String definitionName : beanDefinitionRegister.getBeanDefinitionNames()) {
            final BeanDefinition beanDefinition = beanDefinitionRegister.getBeanDefinition(definitionName);
            final Field[] fields = beanDefinition.getBeanClass().getDeclaredFields();
            for (Field field : fields) {
                final Autowired autowired = AnnotationUtils.findAnnotation(field, Autowired.class);
                if (Objects.nonNull(autowired)) {
                    if (!autowired.required()) {
                        // 如果是非必须则注入null
                        beanDefinition.setAttribute(field.getName(), null);
                    } else {
                        final Class<?> type = field.getType();
                        final String beanName = autowired.value();
                        // 如果没有指定BeanID则根据BeanType获取
                        BeanDefinition fieldDefinition = StringUtils.isEmpty(beanName) ? beanDefinitionRegister.getBeanDefinition(type) : beanDefinitionRegister.getBeanDefinition(beanName);
                        if (Objects.isNull(fieldDefinition)) {
                            throw new IocException("Autowired 注解的属性，不是IOC管理Bean：" + type.getName());
                        }
                        beanDefinition.setAttribute(field.getName(), fieldDefinition);
                    }
                }
            }
        }
    }

    /**
     * 注册BeanDefinition
     *
     * @param beanClass BeanClass
     */
    private void registerBeanDefinition(Class<?> beanClass) {
        final Component component = AnnotationUtils.findAnnotation(beanClass, Component.class);
        if (Objects.isNull(component)) {
            return;
        }

        String beanName = component.value();
        String scope = component.scope();

        // 新建BeanDefinition
        final BeanDefinition beanDefinition = new BaseBeanDefinitionImpl(scope)
                .setBeanName(beanNameGenerator.generateName(beanName, beanClass))
                .setBeanClass(beanClass);

        // 注册BeanDefinitionIOC管理
        beanDefinitionRegister.registerBeanDefinition(beanDefinition);

        // 如果是配置类单独标注,后续先实例化
        if (AnnotationUtils.hasAnnotation(beanClass, Configuration.class)) {
            configBeanDefinition.add(beanDefinition);
        }
    }
}
