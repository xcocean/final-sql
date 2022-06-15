package top.lingkang.finalsql.sql;

import top.lingkang.finalsql.base.SqlInterceptor;
import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.dialect.SqlDialect;
import top.lingkang.finalsql.sql.core.FinalSqlManage;

import java.sql.Connection;

/**
 * @author lingkang
 * Created by 2022/5/22
 * 分页拦截器
 */
public class FinalPageInterceptor implements SqlInterceptor {
    private SqlConfig sqlConfig;
    private FinalSqlManage finalSqlManage;

    public FinalPageInterceptor(SqlConfig sqlConfig, FinalSqlManage finalSqlManage) {
        this.sqlConfig = sqlConfig;
        this.finalSqlManage = finalSqlManage;
    }

    @Override
    public void before(ExSqlEntity sqlEntity, Connection connection) {
        if (finalSqlManage.IS_START.get() != null) {
            finalSqlManage.IS_START.remove();
            PageInfo pageInfo = finalSqlManage.PAGE_INFO_THREAD_LOCAL.get();
            SqlDialect sqlDialect = sqlConfig.getSqlDialect();
            ExSqlEntity totalSql = sqlDialect.total(sqlEntity);
            Long totals = finalSqlManage.selectForObject(totalSql.getSql(), Long.class, totalSql.getParam());
            pageInfo.setTotal(totals);
            finalSqlManage.PAGE_INFO_THREAD_LOCAL.set(pageInfo);
            String s = sqlDialect.rowSql(sqlEntity.getSql(), (pageInfo.getPage() - 1) * pageInfo.getSize(), pageInfo.getSize());
            sqlEntity.setSql(s);
        }
    }

    @Override
    public void after(ExSqlEntity sqlEntity, Connection connection, Object result) {

    }
}
