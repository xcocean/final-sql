package top.lingkang.finalsql.example.sb.test.mysql;

import top.lingkang.finalsql.example.sb.entity.MyUser;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test11Select extends TestBase {
    public static void main(String[] args) throws Exception {

        // 查询对象列表， 约定大于配置， create_time 在返回结果时将被转化为 createTime 驼峰命名
        finalSql.selectForList("select id,username,create_time from user", MyUser.class);

        // 查询对象， 约定大于配置， create_time 在返回结果时将被转化为 createTime 驼峰命名
        finalSql.selectForObject("select * from user", MyUser.class);

        // 查询 Map， 约定大于配置， create_time 在返回结果时将被转化为 createTime 驼峰命名
        finalSql.selectForMap("select id,username,create_time from user");

        // 查询返回指定行
        finalSql.selectForListRow("select * from user", MyUser.class, 2);

    }
}
