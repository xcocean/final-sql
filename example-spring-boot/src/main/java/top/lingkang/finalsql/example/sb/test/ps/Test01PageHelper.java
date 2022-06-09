package top.lingkang.finalsql.example.sb.test.ps;

import java.util.List;
import java.util.Map;

/**
 * @author lingkang
 * Created by 2022/6/9
 */
public class Test01PageHelper extends TestBasePostgresql {
    public static void main(String[] args) {
        finalSql.startPage(5,2);
        List<Map> maps = finalSql.selectForList("select * from users", Map.class);
        System.out.println(maps);
        System.out.println(finalSql.getPageInfo());
        System.out.println(maps.size());
    }
}
