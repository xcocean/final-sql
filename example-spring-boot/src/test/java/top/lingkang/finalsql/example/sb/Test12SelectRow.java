package top.lingkang.finalsql.example.sb;

import top.lingkang.finalsql.example.sb.entity.MyUser;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test12SelectRow extends TestBase {
    public static void main(String[] args) throws Exception {
        init();
        finalSql.selectForListRow("select * from user", MyUser.class, 2);
    }
}
