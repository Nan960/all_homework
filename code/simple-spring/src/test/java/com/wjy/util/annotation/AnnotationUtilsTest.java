package com.wjy.util.annotation;

import com.wjy.ioc.annotation.Autowired;
import com.wjy.ioc.annotation.Component;
import com.wjy.ioc.annotation.Service;
import org.junit.Test;

/**
 * @author wjy
 * @date 2020/9/10
 */
public class AnnotationUtilsTest {

    @Test
    public void testAnno() {
        final Class<Demo1> demo1Class = Demo1.class;

        final Service service = AnnotationUtils.findAnnotation(demo1Class, Service.class);
        System.out.println(service);
        final Autowired autowired = AnnotationUtils.findAnnotation(demo1Class, Autowired.class);
        System.out.println(autowired);
        final Component component = AnnotationUtils.findAnnotation(demo1Class, Component.class);
        System.out.println(component);

        // final Annotation[] annotations = demo1Class.getDeclaredAnnotations();
        // for (Annotation annotation : annotations) {
        //     System.out.println(annotation);
        // }
        // System.out.println(demo1Class.getDeclaredAnnotation(Component.class).toString());
    }

}
