package top.lingkang.finalsql.example.sb.mysql;

import top.lingkang.finalsql.example.sb.mapper.MyMapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test14MapperSelect extends TestBase {
    public static void main(String[] args) throws Exception {
        MyMapper mapper = finalSql.getMapper(MyMapper.class);
        List<Map> userVos = mapper.getMap();
        System.out.println(userVos);
        Date anInt = mapper.getInt();
    }
}
