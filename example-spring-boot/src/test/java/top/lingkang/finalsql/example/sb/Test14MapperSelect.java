package top.lingkang.finalsql.example.sb;

import top.lingkang.finalsql.example.sb.mapper.MyMapper;
import top.lingkang.finalsql.example.sb.vo.UserVo;

import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test14MapperSelect extends TestBase {
    public static void main(String[] args) throws Exception {
        init();
        MyMapper mapper = finalSql.getMapper(MyMapper.class);
        List<UserVo> userVos = mapper.selectList();
        System.out.println(userVos);
    }
}
