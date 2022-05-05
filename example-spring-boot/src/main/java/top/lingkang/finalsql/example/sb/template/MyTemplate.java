package top.lingkang.finalsql.example.sb.template;

import top.lingkang.finalsql.annotation.Param;
import top.lingkang.finalsql.example.sb.entity.MyUser;

import java.util.List;

/**
 * @author lingkang
 * Created by 2022/5/5
 */
public interface MyTemplate {

    MyUser select(@Param("id") Integer id);

    MyUser selectUser(@Param("user")MyUser user);
}
