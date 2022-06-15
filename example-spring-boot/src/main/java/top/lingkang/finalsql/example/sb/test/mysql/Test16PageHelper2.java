package top.lingkang.finalsql.example.sb.test.mysql;

import top.lingkang.finalsql.sql.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test16PageHelper2 extends TestBase {
    public static void main(String[] args) throws Exception {

        finalSql.startPage(1, 20);// 开始分页，依赖于方言的实现
        List<Map> objects = finalSql.selectForList(
                "select id,(select username from user where u.id=id and username=?) from user u order by id desc",
                Map.class,
                "lingkang"
        );
        PageInfo pageInfo = finalSql.getPageInfo();// 获取分页返回的total信息

        System.out.println(pageInfo);
        System.out.println(objects.size());
    }
}
