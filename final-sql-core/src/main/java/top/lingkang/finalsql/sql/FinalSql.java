package top.lingkang.finalsql.sql;

import top.lingkang.finalsql.annotation.Nullable;
import top.lingkang.finalsql.error.FinalException;

import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/12
 */
public interface FinalSql {
    /**
     * 查询
     *
     * @param entity
     * @return
     */
    <T> List<T> select(T entity);

    /**
     * 条件查询
     *
     * @param entity
     * @param condition 条件
     * @return
     */
    <T> List<T> select(T entity, Condition condition);

    /**
     * 查询返回一行
     *
     * @param entity
     * @return
     */
    <T> T selectOne(T entity);

    /**
     * 查询返回一行
     *
     * @param entity
     * @param condition 条件
     * @return
     */
    <T> T selectOne(T entity, Condition condition);

    /**
     * 总数查询
     *
     * @param entity
     * @return
     */
    <T> int selectCount(T entity);

    /**
     * 总数查询
     *
     * @param entity
     * @param condition 条件
     * @return
     */
    <T> int selectCount(T entity, Condition condition);

    /**
     * 插入数据
     *
     * @param entity
     * @return
     */
    <T> int insert(T entity);

    /**
     * 更新数据
     *
     * @param entity
     * @return
     */
    <T> int update(T entity);

    /**
     * 更新数据
     *
     * @param entity
     * @param condition 条件
     * @return
     */
    <T> int update(T entity, Condition condition);

    /**
     * 删除数据
     *
     * @param entity
     * @return
     */
    <T> int delete(T entity);

    /**
     * 删除数据
     *
     * @param entity
     * @param condition 条件
     * @return
     */
    <T> int delete(T entity, Condition condition);

    /**
     * 执行原生查询SQL语句，可能会返回空 ArrayList，需要注意SQL注入
     *
     * @param sql
     * @param callback
     * @return new ArrayList()
     * @throws FinalException
     */
    <T> List<T> nativeSelect(String sql, ResultCallback<T> callback) throws FinalException;

    /**
     * 执行原生查询SQL语句，可能会返回空 ArrayList，
     * 在SQL中使用 ? 代表入参，可防止SQL注入风险
     *
     * @param sql
     * @param callback
     * @param param
     * @param <T>
     * @return
     * @throws FinalException
     */
    <T> List<T> nativeSelect(String sql, ResultCallback<T> callback, Object... param) throws FinalException;
}
