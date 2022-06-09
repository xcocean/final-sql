package top.lingkang.finalsql.example.sb.test.mysql;

import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.dev.FinalSqlDevDataSource;
import top.lingkang.finalsql.example.sb.entity.MyUser;
import top.lingkang.finalsql.sql.FinalSql;
import top.lingkang.finalsql.sql.core.FinalSqlManage;

import javax.sql.DataSource;

/**
 * @author lingkang
 * Created by 2022/4/17
 */
public class Test05 {
    public static void main(String[] args) {
        DataSource dataSource=new FinalSqlDevDataSource(
                "com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://localhost:3306/test?serverTimezone=UTC",
                "root",
                "123456"
        );
        SqlConfig sqlConfig = new SqlConfig(dataSource);
        FinalSql finalSql = new FinalSqlManage(sqlConfig);
        MyUser select = finalSql.selectOne(new MyUser());
        System.out.println(select);
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

    /*private static boolean loadDriver() {
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
    }*/
}
