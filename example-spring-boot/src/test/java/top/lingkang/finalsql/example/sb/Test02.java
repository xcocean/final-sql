package top.lingkang.finalsql.example.sb;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import top.lingkang.finalsql.sql.FinalSql;
import top.lingkang.finalsql.sql.core.FinalSqlManage;
import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.example.sb.entity.MyUser;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/12
 */
public class Test02 {
    //表示数据库的用户名
    private static final String USERNAME = "root";
    //表示数据库的密码
    private static final String PASSWORD = "123456";
    //数据库的驱动信息
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    //访问数据库的地址
    private static final String URL = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";

    public static void main(String[] args) {
        DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        SqlConfig sqlConfig = new SqlConfig(dataSource);
        sqlConfig.setShowSqlLog(true);
        FinalSql finalSql = new FinalSqlManage(sqlConfig);
        MyUser user = new MyUser();
        user.setId(2);
        user.setUsername("lk");
        finalSql.selectOne(user);
        System.out.println(user);

        List<MyUser> query = finalSql.select(user);
        System.out.println(query);

    }
}
