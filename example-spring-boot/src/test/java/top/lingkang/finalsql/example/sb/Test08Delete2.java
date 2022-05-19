package top.lingkang.finalsql.example.sb;

import top.lingkang.finalsql.example.sb.entity.MyUser;
import top.lingkang.finalsql.sql.Condition;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test08Delete2 extends TestBase {
    public static void main(String[] args) throws Exception {
        init();
        System.out.println(finalSql.delete(MyUser.class,new Condition().eq("id",3)));

    }
}
