package top.lingkang.finalsql.example.sb;

import top.lingkang.finalsql.dialect.Mysql57Dialect;
import top.lingkang.finalsql.example.sb.entity.MyUser;
import top.lingkang.finalsql.sql.ExSqlEntity;
import top.lingkang.finalsql.sql.SqlGenerate;

import java.lang.reflect.Field;

/**
 * @author lingkang
 * Created by 2022/4/14
 */
public class Test03 {
    public static void main(String[] args) throws Exception {
        MyUser user = new MyUser();
        user.setUsername("123");
        user.setId(1);
        SqlGenerate sqlGenerate = new SqlGenerate(new Mysql57Dialect());
        ExSqlEntity exSqlEntity = sqlGenerate.insertSql(user);
        System.out.println("insertSql: " + exSqlEntity);


        exSqlEntity = sqlGenerate.updateSql(user,null);
        System.out.println("updateSql: " + exSqlEntity);

        exSqlEntity = sqlGenerate.querySql(user,null);
        System.out.println("querySql: " + exSqlEntity);

        exSqlEntity = sqlGenerate.oneSql(user,null);
        System.out.println("oneSql: " + exSqlEntity);

        exSqlEntity = sqlGenerate.countSql(user,null);
        System.out.println("countSql: " + exSqlEntity);

    }
}
