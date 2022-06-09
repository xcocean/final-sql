package top.lingkang.finalsql.example.sb.mysql;

import top.lingkang.finalsql.example.sb.entity.MyUser;

import java.util.Map;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test12SelectRow extends TestBase {
    public static void main(String[] args) throws Exception {
        // 查询返回对象
        // 约定大于配置， create_time 在返回结果时将被转化为 createTime
        finalSql.selectForListRow("select id,username,create_time from user", MyUser.class, 2);

        // 查询返回 Map
        // 约定大于配置， create_time 在返回结果时将被转化为 createTime
        finalSql.selectForListRow("select id,username,create_time from user", Map.class, 2);
    }
}
