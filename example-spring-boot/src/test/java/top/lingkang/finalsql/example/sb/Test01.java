package top.lingkang.finalsql.example.sb;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import top.lingkang.finalsql.FinalSql;
import top.lingkang.finalsql.example.sb.entity.MyUser;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
@SpringBootTest
@Import(JdbcTemplateAutoConfiguration.class)
public class Test01 {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test01(){
        FinalSql finalSql=new FinalSql(jdbcTemplate.getDataSource());
        List query1 = finalSql.query(MyUser.class);
        System.out.println(query1);
    }
}
