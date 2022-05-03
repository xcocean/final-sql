package top.lingkang.finalsql.sql;

/**
 * @author lingkang
 * Created by 2022/5/3
 */
public interface FinalSqlMapper {
    <T> T getMapper(Class<T> clazz);
}
