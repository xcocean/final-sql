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
    public static void main(String[] args) throws Exception{
        System.out.println(MyUser.class);
        Object user=new MyUser();
        System.out.println(user);
        System.out.println(new MyUser());
        Class<?> myUserClass = MyUser.class;
        System.out.println(myUserClass.newInstance());

        SqlGenerate sqlGenerate=new SqlGenerate(new Mysql57Dialect());
        ExSqlEntity exSqlEntity = sqlGenerate.countSql(MyUser.class);
        System.out.println(exSqlEntity);

        System.out.println(user instanceof Class);
        System.out.println(MyUser.class instanceof Class);


    }
}
