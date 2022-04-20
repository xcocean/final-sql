package top.lingkang.finalsql.example.sb;

import top.lingkang.finalsql.sql.ResultCallback;

import java.sql.ResultSet;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test09Select extends TestBase {
    public static void main(String[] args) throws Exception {
        init();
        Integer integer = finalSql.selectForObject("select count(*) from user", Integer.class);
        System.out.println(integer);
        /*List<MyUser> select = finalSql.select(new MyUser());
        System.out.println(select);*/
        System.out.println(finalSql.nativeSelect("select id from user where id<?", new ResultCallback<Integer>() {
            @Override
            public Integer callback(ResultSet result) throws Exception {
                return result.getInt(1);
            }
        },666666));
    }
}
