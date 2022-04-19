package top.lingkang.finalsql.example.sb;

import top.lingkang.finalsql.utils.ClassUtils;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test09Select extends TestBase{
    public static void main(String[] args) throws Exception{
        init();
        Integer integer = finalSql.selectForObject("select count(*) from user", Integer.class);
        System.out.println(integer);
    }
}
