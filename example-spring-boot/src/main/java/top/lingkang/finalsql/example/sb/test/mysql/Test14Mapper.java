package top.lingkang.finalsql.example.sb.test.mysql;

import top.lingkang.finalsql.example.sb.mapper.MyMapper;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test14Mapper extends TestBase {
    public static void main(String[] args) throws Exception {
        MyMapper mapper = finalSql.getMapper(MyMapper.class);
        mapper.select(530368L);

        MyMapper mapper1 = finalSql.getMapper(MyMapper.class);
        mapper1.select(530368L);
    }
}
