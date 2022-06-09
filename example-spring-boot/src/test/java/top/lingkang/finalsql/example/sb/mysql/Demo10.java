package top.lingkang.finalsql.example.sb.mysql;

/**
 * @author lingkang
 * Created by 2022/5/22
 */
public class Demo10 {
    public static void main(String[] args) {
        String sql="select * from user order by id";
        System.out.println(sql.substring(sql.indexOf("from")));
    }
}
