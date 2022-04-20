package top.lingkang.finalsql.example.sb;

import top.lingkang.finalsql.example.sb.entity.MyUser;
import top.lingkang.finalsql.sql.Condition;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test11Select extends TestBase {
    public static void main(String[] args) throws Exception {
        init();
        System.out.println(finalSql.selectForList("select * from user", MyUser.class));
        System.out.println(finalSql.selectCount(MyUser.class));
        System.out.println(finalSql.selectForObject("select * from user", MyUser.class));
    }
}
