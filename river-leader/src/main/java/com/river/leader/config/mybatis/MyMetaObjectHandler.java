package com.river.leader.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

/**
 *  注入公共字段自动填充,任选注入方式即可
 */
public class MyMetaObjectHandler implements MetaObjectHandler {

    protected final static Logger logger = LoggerFactory.getLogger(MyMetaObjectHandler.class);

    @Override
    public void insertFill(MetaObject metaObject) {
        logger.info("新增的时候干点不可描述的事情");
        // 更多查看源码测试用例
        System.out.println("*************************");
        System.out.println("insert fill");
        System.out.println("*************************");

        // 测试下划线
        Object testType = getFieldValByName("corpCode", metaObject);//mybatis-plus版本2.0.9+
        System.out.println("corpCode=" + testType);
        if (testType == null) {
            setFieldValByName("creatorUserId", 1L, metaObject);
            setFieldValByName("creationTime", new Timestamp(System.currentTimeMillis()), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        logger.info("更新的时候干点不可描述的事情");
        //更新填充
        System.out.println("*************************");
        System.out.println("update fill");
        System.out.println("*************************");
        //mybatis-plus版本2.0.9+
        setFieldValByName("lastModifierUserId", 2L, metaObject);
        setFieldValByName("lastModifyTime", new Timestamp(System.currentTimeMillis()), metaObject);

    }
}
