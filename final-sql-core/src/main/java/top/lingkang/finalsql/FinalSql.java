package top.lingkang.finalsql;

import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/12
 */
public interface FinalSql<T> {
    List<T> query(T entity);

    T queryOne(T entity);
}
