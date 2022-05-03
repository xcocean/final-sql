package top.lingkang.finalsql.example.sb.mapper;

import top.lingkang.finalsql.annotation.Select;

/**
 * @author lingkang
 * Created by 2022/5/2
 */
public interface MyMapper {
    @Select("select id from user where id in (?,?)")
    Integer select(Object... obj);
}
