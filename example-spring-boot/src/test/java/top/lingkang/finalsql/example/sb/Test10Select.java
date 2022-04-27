package top.lingkang.finalsql.example.sb;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test10Select extends TestBase {
    public static void main(String[] args) throws Exception {
        init();
        System.out.println(finalSql.selectForMap("select * from user"));
        System.out.println(finalSql.selectForMap("select * from user", true));
        List<Integer> list=new ArrayList<>();
        list.add(35);
        System.out.println(finalSql.selectForMap("select * from user where id=?", true, list));
    }
}
