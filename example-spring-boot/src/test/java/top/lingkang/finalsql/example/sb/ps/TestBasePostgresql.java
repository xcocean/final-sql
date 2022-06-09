package top.lingkang.finalsql.example.sb.ps;

import com.zaxxer.hikari.HikariDataSource;
import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.dev.FinalSqlDevDataSource;
import top.lingkang.finalsql.sql.FinalSql;
import top.lingkang.finalsql.sql.core.FinalSqlManage;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class TestBasePostgresql {
    protected static FinalSqlDevDataSource dataSource;
    protected static FinalSql finalSql;

    static {
        init();
    }

    public static void init() {
        /*HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setJdbcUrl("jdbc:postgresql://10.8.4.191:5432/test");
        ds.setUsername("postgres");
        ds.setPassword("123456");*/
        dataSource=new FinalSqlDevDataSource(
                "org.postgresql.Driver",
                "jdbc:postgresql://10.8.4.191:5432/test",
                "postgres",
                "123456"
        );

        SqlConfig sqlConfig = new SqlConfig(dataSource);
        sqlConfig.setShowLog(true);
        finalSql = new FinalSqlManage(sqlConfig);
    }
}
