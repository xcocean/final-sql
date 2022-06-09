package top.lingkang.finalsql.example.sb.mysql;

import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.dev.FinalSqlDevDataSource;
import top.lingkang.finalsql.example.sb.entity.MyUser;
import top.lingkang.finalsql.sql.FinalSql;
import top.lingkang.finalsql.sql.core.FinalSqlManage;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test07 {
    public static void main(String[] args) {
        DataSource dataSource=new FinalSqlDevDataSource();
        SqlConfig sqlConfig = new SqlConfig(dataSource);
        FinalSql finalSql = new FinalSqlManage(sqlConfig);
        List<MyUser> myUsers = finalSql.selectForList("select * from user", MyUser.class);
        System.out.println(myUsers);
    }
}
