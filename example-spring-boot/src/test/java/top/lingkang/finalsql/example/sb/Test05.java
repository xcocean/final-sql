package top.lingkang.finalsql.example.sb;

import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.dev.FinalSqlDevDataSource;
import top.lingkang.finalsql.example.sb.entity.MyUser;
import top.lingkang.finalsql.sql.FinalSql;
import top.lingkang.finalsql.sql.ResultCallback;
import top.lingkang.finalsql.sql.impl.FinalSqlImpl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/17
 */
public class Test05 {
    private static Connection conn=null;
    public static void main(String[] args) {
        loadDriver();
        DataSource dataSource=new FinalSqlDevDataSource(conn);
        SqlConfig sqlConfig = new SqlConfig(dataSource);
        //sqlConfig.setShowSqlLog(true);
        FinalSql finalSql = new FinalSqlImpl(sqlConfig);

       /* List list = finalSql.nativeSelect("select * from user", new ResultCallback<MyUser>() {
            @Override
            public MyUser callback(ResultSet result) throws SQLException {
                MyUser user = new MyUser();
                user.setId(result.getInt(1));
                user.setUsername(result.getString(2));
                return user;
            }
        });
        System.out.println(list);*/
    }

    private static boolean loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("注册驱动成功");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/test?serverTimezone=UTC",
                    "root",
                    "123456");
            System.out.println("链接数据库成功");
            return true;
        } catch (Exception e) {
            System.out.println("链接数据库失败");
            return false;
        }
    }
}
