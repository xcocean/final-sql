package top.lingkang.finalsql.example.sb.test.mysql;

import top.lingkang.finalsql.example.sb.entity.MyUser;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test09Insert2 extends TestBase{
    public static void main(String[] args) throws Exception{
        finalSql.insert(MyUser.class);
    }
}
