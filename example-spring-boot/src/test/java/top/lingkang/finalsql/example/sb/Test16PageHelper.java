package top.lingkang.finalsql.example.sb;

import top.lingkang.finalsql.example.sb.entity.MyUser;
import top.lingkang.finalsql.sql.FinalPageHelper;
import top.lingkang.finalsql.sql.PageInfo;
import top.lingkang.finalsql.sql.ResultCallback;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test16PageHelper extends TestBase {
    public static void main(String[] args) throws Exception {
        init();

        FinalPageHelper.startPage(2,10);// 开始分页，依赖于方言的实现
        List<Map> objects = finalSql.selectForList("select * from user order by id desc",Map.class);
        PageInfo pageInfo = FinalPageHelper.getPageInfo();// 获取分页返回的total信息

        System.out.println(pageInfo);
        System.out.println(objects.size());
    }
}