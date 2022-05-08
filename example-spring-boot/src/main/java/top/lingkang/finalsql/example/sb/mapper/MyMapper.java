package top.lingkang.finalsql.example.sb.mapper;

import top.lingkang.finalsql.annotation.Delete;
import top.lingkang.finalsql.annotation.Insert;
import top.lingkang.finalsql.annotation.Select;
import top.lingkang.finalsql.annotation.Update;
import top.lingkang.finalsql.example.sb.entity.MyUser;
import top.lingkang.finalsql.example.sb.vo.UserVo;

import java.util.List;
import java.util.Map;

/**
 * @author lingkang
 * Created by 2022/5/2
 */
public interface MyMapper {
    @Select("select id from user where id in (?)")
    int select(Long id);

    @Select
    List selectByObj(Object... obj);

    @Select("select * from user")
    List<UserVo> selectList();

    @Select("select id as user_id from user limit 1")
    List<Map> getMap();

    @Select("select * from user")
    UserVo user();

    @Insert()
    Integer insert(Object... obj);

    @Update
    int update(Object... obj);

    @Update("update user set create_time=now() where id=?")
    int update(Integer id);

    @Delete("delete from user where id=?")
    int delete(Integer id);

    @Delete
    int deleteUser(MyUser user);
}
