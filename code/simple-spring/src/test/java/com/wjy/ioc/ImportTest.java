package com.wjy.ioc;

import com.wjy.ioc.annotation.Import;
import com.wjy.ioc.annotation.ImportDemo;
import com.wjy.util.annotation.AnnotationUtils;
import org.junit.Test;

import java.lang.annotation.Annotation;

/**
 * @author wjy
 * @date 2020/9/13
 */
public class ImportTest {

    @Test
    public void testImport() {
        // final Import anImport = AnnotationUtils.findAnnotation(ImportDemo.class, Import.class);
        // final Class<?>[] value = anImport.value();
        // System.out.println(value);

        for (Annotation annotation : ImportDemo.class.getDeclaredAnnotations()) {
            System.out.println(annotation);
        }
    }
}