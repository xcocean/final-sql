package top.lingkang.finalsql.example.sb;

import top.lingkang.finalsql.example.sb.entity.MyUser;

import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test09Update extends TestBase{
    public static void main(String[] args) throws Exception{
        init();
        int i = finalSql.nativeUpdate("update user set username='4399' where id='530368'");
        System.out.println(i);
        System.out.println(finalSql.nativeUpdate("update user set username='4399' where id='53036811'"));
    }
}
