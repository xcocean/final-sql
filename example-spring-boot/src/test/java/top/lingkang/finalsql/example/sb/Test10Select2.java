package top.lingkang.finalsql.example.sb;

import top.lingkang.finalsql.example.sb.entity.MyUser;
import top.lingkang.finalsql.sql.Condition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test10Select2 extends TestBase {
    public static void main(String[] args) throws Exception {
        init();
        System.out.println(finalSql.select(MyUser.class,new Condition().eq("id",530368)));
    }
}
