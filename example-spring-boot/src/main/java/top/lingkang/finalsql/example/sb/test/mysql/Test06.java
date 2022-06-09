package top.lingkang.finalsql.example.sb.test.mysql;


import top.lingkang.finalsql.utils.NameUtils;

/**
 * @author lingkang
 * Created by 2022/4/18
 */
public class Test06 {
    public static void main(String[] args) {
        /*String sql="insert into user(username) values (?)";
        System.out.println(sql.replaceFirst("[(]","(id,"));
        System.out.println(sql.replaceFirst("s \\(", "s (select nextval('user'), "));*/
        System.out.println(NameUtils.toHump("u_user"));
        System.out.println(NameUtils.unHump("UUser"));
    }
}
