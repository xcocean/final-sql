package top.lingkang.finalsql.example.sb;

import top.lingkang.finalsql.dialect.Mysql57Dialect;
import top.lingkang.finalsql.example.sb.entity.MyUser;
import top.lingkang.finalsql.sql.Condition;
import top.lingkang.finalsql.sql.ExSqlEntity;
import top.lingkang.finalsql.sql.SqlGenerate;

/**
 * @author lingkang
 * Created by 2022/4/15
 */
public class Test04 {
    public static void main(String[] args) {
        MyUser user = new MyUser();
        user.setUsername("123");
        user.setId(1);
        SqlGenerate sqlGenerate = new SqlGenerate(new Mysql57Dialect());
        ExSqlEntity exSqlEntity = sqlGenerate.oneSql(user,
                new Condition().orderByAsc("id","username")
                .orderByDesc("password")
                .and("id",1)
        );
        System.out.println(exSqlEntity);
    }
}
