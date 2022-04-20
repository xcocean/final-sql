package top.lingkang.finalsql.example.sb;

import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.dev.FinalSqlDevDataSource;
import top.lingkang.finalsql.sql.FinalSql;
import top.lingkang.finalsql.sql.core.FinalSqlManage;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class TestBase {
    protected static FinalSqlDevDataSource dataSource;
    protected static FinalSql finalSql;

    public static void init() {
        dataSource = new FinalSqlDevDataSource();
        SqlConfig sqlConfig = new SqlConfig(dataSource);
        //sqlConfig.setShowSqlLog(true).setShowResultLog(true);
        finalSql = new FinalSqlManage(sqlConfig);
    }
}
