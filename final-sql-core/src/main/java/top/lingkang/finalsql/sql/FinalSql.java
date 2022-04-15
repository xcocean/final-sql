package top.lingkang.finalsql.sql;

import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/12
 */
public interface FinalSql<T> {
    /**
     * 查询
     * @param entity
     * @return
     */
    List<T> select(T entity);

    /**
     * 条件查询
     * @param entity
     * @param condition 条件
     * @return
     */
    List<T> select(T entity, Condition condition);

    /**
     * 查询返回一行
     * @param entity
     * @return
     */
    T selectOne(T entity);

    /**
     * 查询返回一行
     * @param entity
     * @param condition 条件
     * @return
     */
    T selectOne(T entity, Condition condition);

    /**
     * 总数查询
     * @param entity
     * @return
     */
    int selectCount(T entity);

    /**
     * 总数查询
     * @param entity
     * @param condition 条件
     * @return
     */
    int selectCount(T entity, Condition condition);

    /**
     * 插入数据
     * @param entity
     * @return
     */
    int insert(T entity);

    /**
     * 更新数据
     * @param entity
     * @return
     */
    int update(T entity);

    /**
     * 更新数据
     * @param entity
     * @param condition 条件
     * @return
     */
    int update(T entity, Condition condition);

    /**
     * 删除数据
     * @param entity
     * @return
     */
    int delete(T entity);

    /**
     * 删除数据
     * @param entity
     * @param condition 条件
     * @return
     */
    int delete(T entity, Condition condition);
}
