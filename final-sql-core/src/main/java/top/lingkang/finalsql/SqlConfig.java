package top.lingkang.finalsql;

import top.lingkang.finalsql.dialect.Mysql57Dialect;
import top.lingkang.finalsql.dialect.SqlDialect;

import javax.sql.DataSource;

/**
 * @author lingkang
 * Created by 2022/4/12
 */
public class SqlConfig {
    public SqlConfig() {
    }

    public SqlConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private DataSource dataSource;
    private boolean showSqlLog = false;
    private boolean showResultLog = false;
    private SqlDialect sqlDialect = new Mysql57Dialect();


    public DataSource getDataSource() {
        return dataSource;
    }

    public SqlConfig setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public SqlDialect getSqlDialect() {
        return sqlDialect;
    }

    public SqlConfig setSqlDialect(SqlDialect sqlDialect) {
        this.sqlDialect = sqlDialect;
        return this;
    }

    public boolean isShowSqlLog() {
        return showSqlLog;
    }

    public SqlConfig setShowSqlLog(boolean showSqlLog) {
        this.showSqlLog = showSqlLog;
        return this;
    }

    public boolean isShowResultLog() {
        return showResultLog;
    }

    public SqlConfig setShowResultLog(boolean showResultLog) {
        this.showResultLog = showResultLog;
        return this;
    }
}
