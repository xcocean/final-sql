package top.lingkang.finalsql.base.impl;

import top.lingkang.finalsql.base.SqlInterceptor;
import top.lingkang.finalsql.sql.ExSqlEntity;

import java.sql.Connection;

/**
 * @author lingkang
 * Created by 2022/4/18
 */
public class DefaultSqlInterceptor implements SqlInterceptor {

    @Override
    public void before(ExSqlEntity sqlEntity, Connection connection) {
    }

    @Override
    public void after(ExSqlEntity sqlEntity, Connection connection, Object result) {
    }
}
