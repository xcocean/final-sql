package top.lingkang.finalsql.example.sb.mysql;

import top.lingkang.finalsql.example.sb.entity.MyUser;
import top.lingkang.finalsql.example.sb.mapper.MyMapper;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test14MapperDelete extends TestBase {
    public static void main(String[] args) throws Exception {
        MyMapper mapper = finalSql.getMapper(MyMapper.class);
        //mapper.update(568277);
        MyUser user=new MyUser();
        user.setId(6);
        mapper.delete(1);
    }
}
