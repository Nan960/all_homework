package com.lagou.edu.transaction;

import com.wjy.ioc.annotation.Autowired;
import com.wjy.ioc.annotation.Configuration;
import com.wjy.transaction.TransactionManager;

import java.sql.SQLException;

/**
 * 声明式事务支持
 *
 * @author wjy
 * @date 2020/9/13
 */
@Configuration
public class TransactionProcess implements TransactionManager {

    @Autowired
    com.lagou.edu.utils.TransactionManager transactionManager;

    @Override
    public void start() throws SQLException {
        transactionManager.beginTransaction();
    }

    @Override
    public void commit() throws SQLException {
        transactionManager.commit();
    }

    @Override
    public void rollback() throws SQLException {
        transactionManager.rollback();
    }
}
