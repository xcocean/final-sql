package top.lingkang.finalsql.example.sb.mapper;

import top.lingkang.finalsql.annotation.Insert;
import top.lingkang.finalsql.annotation.Select;
import top.lingkang.finalsql.annotation.Update;
import top.lingkang.finalsql.example.sb.entity.MyUser;

import java.util.Date;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/5/2
 */
public interface MyMapper {
    @Select("select id from user where id in (?)")
    int select(Long id);

    @Select("select id from user where id in (?0.id)")
    int selectUser(MyUser user);

    @Select
    List selectByObj(Object... obj);

    @Insert()
    Integer insert(Object... obj);

    @Update
    int update(Object... obj);

    @Update("update user set create_time=now() where id=?")
    int update(Integer id);
}
