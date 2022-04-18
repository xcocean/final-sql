package top.lingkang.finalsql.example.sb;


import top.lingkang.finalsql.example.sb.entity.MyUser;

import java.util.Date;

/**
 * @author lingkang
 * Created by 2022/4/18
 */
public class Test06 {
    public static void main(String[] args) {
        String sql="insert into user(username) values (?)";
        System.out.println(sql.replaceFirst("[(]","(id,"));
        System.out.println(sql.replaceFirst("s \\(", "s (select nextval('user'), "));
    }
}
