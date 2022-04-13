package top.lingkang.finalsql.example.sb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.lingkang.finalsql.FinalSql;
import top.lingkang.finalsql.SqlConfig;
import top.lingkang.finalsql.impl.FinalSqlImpl;

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
        sqlConfig.setShowSqlLog(true);
        return new FinalSqlImpl<>(sqlConfig);
    }
}