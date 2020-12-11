package com.lagou.edu;

import com.lagou.edu.transaction.TransactionProcess;
import com.wjy.ioc.annotation.Configuration;
import com.wjy.ioc.annotation.Import;
import com.wjy.ioc.annotation.ScanPackage;

/**
 * @author wjy
 * @date 2020/9/13
 */
@Configuration
@ScanPackage({"com.lagou.edu"})
@Import(TransactionProcess.class)
public class SimpleSpringConfig {

}
