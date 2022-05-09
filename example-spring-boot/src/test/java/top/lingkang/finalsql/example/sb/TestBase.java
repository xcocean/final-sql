package top.lingkang.finalsql.example.sb;

import com.zaxxer.hikari.HikariDataSource;
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
        /*HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setJdbcUrl("jdbc:mysql://localhost:3306/test?serverTimezone=UTC");
        ds.setUsername("root");
        ds.setPassword("123456");
*/

        dataSource = new FinalSqlDevDataSource();
        SqlConfig sqlConfig = new SqlConfig(dataSource);
        sqlConfig.setShowLog(true);
        finalSql = new FinalSqlManage(sqlConfig);
    }
}
