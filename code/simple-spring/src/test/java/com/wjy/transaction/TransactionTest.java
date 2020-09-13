package com.wjy.transaction;

import com.wjy.util.annotation.AnnotationUtils;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @author wjy
 * @date 2020/9/13
 */
public class TransactionTest {

    @Test
    public void testTransactionAnnotation() {
        for (Method method : DemoTransaction.class.getMethods()) {
            final Transaction transaction = AnnotationUtils.findAnnotation(method, Transaction.class);
            System.out.println(transaction);
        }

    }
}
