package top.lingkang.finalsql.example.sb.test.mysql;

import top.lingkang.finalsql.example.sb.entity.MyUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test09Insert extends TestBase{
    public static void main(String[] args) throws Exception{
        List<MyUser> list=new ArrayList<>();
        for (int i=0;i<5;i++){
            MyUser user=new MyUser();
            user.setUsername("lk"+i);
            user.setCreateTime(new Date());
            list.add(user);
        }
        System.out.println(finalSql.batchInsert(list));
    }
}
