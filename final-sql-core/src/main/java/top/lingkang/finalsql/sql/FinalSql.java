package top.lingkang.finalsql.sql;

import top.lingkang.finalsql.annotation.Nullable;
import top.lingkang.finalsql.error.FinalException;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/12
 * 对外操作接口
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
     * 查询
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> List<T> select(Class<T> entity);

    /**
     * 条件查询
     *
     * @param entity
     * @param condition 条件
     * @return
     */
    <T> List<T> select(T entity, Condition condition);

    /**
     * 条件查询
     *
     * @param entity
     * @param condition
     * @param <T>
     * @return
     */
    <T> List<T> select(Class<T> entity, Condition condition);

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
     * @param <T>
     * @return
     */
    <T> T selectOne(Class<T> entity);

    /**
     * 查询返回一行
     *
     * @param entity
     * @param condition 条件
     * @return
     */
    <T> T selectOne(T entity, Condition condition);

    /**
     * 查询返回一行
     *
     * @param entity
     * @param condition
     * @param <T>
     * @return
     */
    <T> T selectOne(Class<T> entity, Condition condition);

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
     * @param <T>
     * @return
     */
    <T> int selectCount(Class<T> entity);

    /**
     * 总数查询
     *
     * @param entity
     * @param condition 条件
     * @return
     */
    <T> int selectCount(T entity, Condition condition);

    /**
     * 总数查询
     *
     * @param entity
     * @param condition
     * @param <T>
     * @return
     */
    <T> int selectCount(Class<T> entity, Condition condition);

    /**
     * 执行SQL查询返回对象
     *
     * @param sql
     * @param t
     * @param <T>
     * @return
     */
    <T> List<T> selectForList(String sql, Class<T> t);


    /**
     * 执行SQL查询返回对象
     *
     * @param sql
     * @param t
     * @param param sql中的 ? 入参
     * @param <T>
     * @return
     */
    <T> List<T> selectForList(String sql, Class<T> t, Object... param);

    /**
     * 执行SQL返回对象
     *
     * @param sql
     * @param t
     * @param <T>
     * @return
     */
    @Nullable
    <T> T selectForObject(String sql, Class<T> t);

    /**
     * 执行SQL返回对象
     *
     * @param sql
     * @param t
     * @param param
     * @param <T>
     * @return
     */
    @Nullable
    <T> T selectForObject(String sql, Class<T> t, Object... param);

    /**
     * 插入数据
     *
     * @param entity
     * @return
     */
    <T> int insert(T entity);

    /**
     * 批量插入
     *
     * @param entity
     * @param <T>
     * @return
     * @throws FinalException
     */
    <T> int batchInsert(List<T> entity) throws FinalException;

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
     * 根据id删除数据
     *
     * @param entity
     * @param ids
     * @param <T>
     * @return
     */
    <T> int deleteByIds(Class<T> entity, Object... ids);

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

    /**
     * 执行原生更新SQL
     *
     * @param sql
     * @return
     * @throws FinalException
     */
    int nativeUpdate(String sql) throws FinalException;

    /**
     * 执行原生更新SQL
     *
     * @param sql
     * @param param
     * @return
     * @throws FinalException
     */
    int nativeUpdate(String sql, Object... param) throws FinalException;

    /**
     * 获取 DataSource
     *
     * @return
     */
    DataSource getDataSource();
}
