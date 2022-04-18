package top.lingkang.finalsql.base;

import top.lingkang.finalsql.sql.ExSqlEntity;

/**
 * @author lingkang
 * Created by 2022/4/18
 */
public interface SqlInterceptor {
    /**
     * 执行之前拦截SQL，可进一步进行处理
     *
     * @param sqlEntity
     */
    void before(ExSqlEntity sqlEntity);

    /**
     * 执行SQL之后的结果拦截器
     *
     * @param sqlEntity
     * @param result
     */
    void after(ExSqlEntity sqlEntity, Object result);
}
