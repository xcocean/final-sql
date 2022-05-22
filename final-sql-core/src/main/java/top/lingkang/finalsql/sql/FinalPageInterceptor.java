package top.lingkang.finalsql.sql;

import top.lingkang.finalsql.base.SqlInterceptor;
import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.dialect.SqlDialect;

import java.sql.Connection;

/**
 * @author lingkang
 * Created by 2022/5/22
 */
public class FinalPageInterceptor implements SqlInterceptor {
    private SqlConfig sqlConfig;
    private FinalSql finalSql;

    public FinalPageInterceptor(SqlConfig sqlConfig, FinalSql finalSql) {
        this.sqlConfig = sqlConfig;
        this.finalSql = finalSql;
    }

    @Override
    public void before(ExSqlEntity sqlEntity, Connection connection) {
        if (FinalPageHelper.IS_START.get() != null) {
            FinalPageHelper.IS_START.remove();
            PageInfo info = FinalPageHelper.PAGE_INFO_THREAD_LOCAL.get();
            SqlDialect sqlDialect = sqlConfig.getSqlDialect();
            String sql = sqlDialect.total(sqlEntity.getSql());
            Long totals = finalSql.selectForObject(sql, Long.class, sqlEntity.getParam());
            info.setTotal(totals);
            FinalPageHelper.PAGE_INFO_THREAD_LOCAL.set(info);
            String s = sqlDialect.rowSql(sqlEntity.getSql(), (info.getPage() - 1) * info.getSize(), info.getSize());
            sqlEntity.setSql(s);
        }
    }

    @Override
    public void after(ExSqlEntity sqlEntity, Connection connection, Object result) {

    }
}
