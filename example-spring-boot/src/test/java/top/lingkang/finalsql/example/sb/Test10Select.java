package top.lingkang.finalsql.example.sb;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test10Select extends TestBase {
    public static void main(String[] args) throws Exception {
        init();
        System.out.println(finalSql.selectForMap("select * from user"));
        System.out.println(finalSql.selectForMap("select * from user", true));
        System.out.println(finalSql.selectForMap("select * from user where id=?", true, 530368));
    }
}
