package top.lingkang.finalsql.example.sb.mapper;

import top.lingkang.finalsql.annotation.Insert;
import top.lingkang.finalsql.annotation.Select;
import top.lingkang.finalsql.annotation.Update;

import java.util.List;

/**
 * @author lingkang
 * Created by 2022/5/2
 */
public interface MyMapper {
    @Select("select id from user where id in (?)")
    Integer select(Object... obj);

    @Select
    List selectByObj(Object... obj);

    @Insert()
    Integer insert(Object... obj);

    @Update
    int update(Object... obj);

    @Update("update user set create_time=now() where id=?")
    int update(Integer id);
}
