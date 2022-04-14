package top.lingkang.finalsql.dev;

import top.lingkang.finalsql.config.SqlConfig;

import javax.sql.DataSource;

/**
 * @author lingkang
 * Created by 2022/4/12
 */
public class SqlConfigDev extends SqlConfig {
    public SqlConfigDev(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public boolean isShowSqlLog() {
        return true;
    }

    @Override
    public boolean isShowResultLog() {
        return true;
    }
}
