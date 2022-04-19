package top.lingkang.finalsql.example.sb;

import top.lingkang.finalsql.example.sb.entity.MyUser;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test08Delete extends TestBase {
    public static void main(String[] args) throws Exception {
        init();
        System.out.println(finalSql.deleteByIds(MyUser.class, 46));
    }
}
