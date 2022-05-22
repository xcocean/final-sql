package top.lingkang.finalsql.example.sb.config;

import cn.beecp.BeeDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
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
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(BeeDataSource.class).build();
    }

    @Bean
    public FinalSql finalSql(@Qualifier("dataSource") DataSource dataSource) {
        SqlConfig sqlConfig = new SqlConfig(dataSource);
        // sqlConfig.setSqlDialect(new PostgreSqlDialect());
        sqlConfig.setShowLog(true);
        // sqlConfig.setUsePageHelper(false);
        // sqlConfig.setShowResultLog(true);
        return new FinalSqlManage(sqlConfig);
    }

    /*private void test(){
        DataSource dataSource= DataSourceUtils
    }*/

    /*@Bean
    public MyMapper myMapper(@Qualifier("finalSql")FinalSql finalSql){
        return finalSql.getMapper(MyMapper.class);
    }

    @Autowired
    private MyMapper myMapper;
    public static void main(String[] args) {

    }*/
}
