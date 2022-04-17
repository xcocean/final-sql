package top.lingkang.finalsql.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author lingkang
 * Created by 2022/4/17
 */
public interface ResultCallback<T> {

    T callback(ResultSet result) throws SQLException;
}
