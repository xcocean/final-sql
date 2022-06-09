package top.lingkang.finalsql.example.sb.mysql;

import top.lingkang.finalsql.example.sb.entity.MyUser;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test08Delete extends TestBase {
    public static void main(String[] args) throws Exception {
        System.out.println(finalSql.deleteByIds(MyUser.class, 46));
        MyUser user=new MyUser();
        user.setId(41);
        user.setUsername("123");
        System.out.println(finalSql.delete(user));

    }
}
