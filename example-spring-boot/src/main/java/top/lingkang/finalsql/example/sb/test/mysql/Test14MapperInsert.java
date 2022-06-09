package top.lingkang.finalsql.example.sb.test.mysql;

import top.lingkang.finalsql.example.sb.entity.MyUser;
import top.lingkang.finalsql.example.sb.mapper.MyMapper;

import java.util.Date;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test14MapperInsert extends TestBase {
    public static void main(String[] args) throws Exception {
        MyMapper mapper = finalSql.getMapper(MyMapper.class);
        MyUser user=new MyUser();
        user.setUsername("lingkang");
        user.setCreateTime(new Date());
        user.setPassword("123456");
        mapper.insert(user);
        System.out.println(user);
    }
}
