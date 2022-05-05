package top.lingkang.finalsql.base;

import top.lingkang.finalsql.sql.ExSqlEntity;

import java.sql.Connection;

/**
 * @author lingkang
 * Created by 2022/4/18
 * 拦截器，可拦截执行之前的SQL和执行之后的结果处理
 */
public interface SqlInterceptor {
    /**
     * 执行之前拦截SQL，可进一步进行处理
     *
     * @param sqlEntity
     * @param connection
     */
    void before(ExSqlEntity sqlEntity, Connection connection);

    /**
     * 执行SQL之后的结果拦截器
     *
     * @param sqlEntity
     * @param connection
     * @param result
     */
    void after(ExSqlEntity sqlEntity, Connection connection, Object result);
}
