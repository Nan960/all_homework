package com.wjy.util;

import com.wjy.demo.Demo1;
import org.junit.Test;

/**
 * @author wjy
 * @date 2020/9/13
 */
public class ReflectionUtilsTest {

    @Test
    public void testSupperClass() {
        final Class<Demo1> aClass = Demo1.class;
        System.out.println(aClass.getSuperclass());
        for (Class<?> anInterface : aClass.getInterfaces()) {
            System.out.println(anInterface);
        }
        System.out.println(aClass.getSuperclass());
        System.out.println(aClass.getSuperclass().equals(Object.class));
        System.out.println(aClass.getSuperclass().getSuperclass());
        System.out.println(aClass.getSuperclass().getSuperclass().equals(Object.class));
    }
}
