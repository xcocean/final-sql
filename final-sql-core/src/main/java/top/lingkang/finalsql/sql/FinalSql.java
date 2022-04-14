package top.lingkang.finalsql.sql;

import top.lingkang.finalsql.transaction.FinalTransactionHolder;

import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/12
 */
public interface FinalSql<T> {
    List<T> select(T entity);

    T selectOne(T entity);

    int selectCount(T entity);

    int insert(T entity);

    int update(T entity);

    int update(T entity,Condition condition);
}
