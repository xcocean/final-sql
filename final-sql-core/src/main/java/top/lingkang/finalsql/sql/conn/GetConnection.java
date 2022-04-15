package top.lingkang.finalsql.sql.conn;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lingkang
 * Created by 2022/4/15
 */
public interface GetConnection {
    Connection get() throws SQLException;
}
