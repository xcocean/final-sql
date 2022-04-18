package top.lingkang.finalsql.example.sb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.sql.FinalSql;
import top.lingkang.finalsql.sql.core.FinalSqlManage;

import javax.sql.DataSource;

/**
 * @author lingkang
 * Created by 2022/4/13
 */
@Configuration
public class FinalSqlConfig {
    @Autowired
    private DataSource dataSource;

    @Bean
    public FinalSql finalSql() {
        SqlConfig sqlConfig = new SqlConfig(dataSource);
        // sqlConfig.setSqlDialect(new PostgreSqlDialect());
        sqlConfig.setShowSqlLog(true);
        // sqlConfig.setShowResultLog(true);
        return new FinalSqlManage(sqlConfig);
    }

    /*private void test(){
        DataSource dataSource= DataSourceUtils
    }*/
}
