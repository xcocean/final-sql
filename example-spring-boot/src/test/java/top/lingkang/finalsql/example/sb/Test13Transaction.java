package top.lingkang.finalsql.example.sb;

import top.lingkang.finalsql.error.FinalException;
import top.lingkang.finalsql.example.sb.entity.MyUser;

import java.util.Map;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test13Transaction extends TestBase {
    public static void main(String[] args) throws Exception {
        init();
        finalSql.begin();
        finalSql.begin();
        try {
            finalSql.nativeUpdate("update user set num=? where id=?",66,6);
            if (1==1)
                throw new RuntimeException("1");
        } catch (FinalException e) {
            finalSql.rollback();
        }
    }
}
