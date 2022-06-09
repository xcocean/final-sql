package top.lingkang.finalsql.example.sb.test.ps;

import top.lingkang.finalsql.example.sb.test.ps.entity.MyUsers;

import java.util.Date;

/**
 * @author lingkang
 * Created by 2022/6/9
 */
public class Test02Insert extends TestBasePostgresql {
    public static void main(String[] args) {
        MyUsers myUsers=new MyUsers();
        myUsers.setId(99);
        myUsers.setNum(1);
        myUsers.setUsername("lingkang");
        myUsers.setCreateTime(new Date());
        myUsers.setPassword("asfsadas");
        finalSql.insert(myUsers);
        System.out.println(myUsers);
    }
}
