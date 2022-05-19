package top.lingkang.finalsql.example.sb;

import top.lingkang.finalsql.example.sb.entity.MyUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test09Insert2 extends TestBase{
    public static void main(String[] args) throws Exception{
        init();
        finalSql.insert(MyUser.class);
    }
}
