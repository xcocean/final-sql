package top.lingkang.finalsql.sql.conn;

import top.lingkang.finalsql.transaction.FinalTransactionHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lingkang
 * Created by 2022/4/15
 */
public class DefaultGetConnection implements GetConnection {
    private static final ThreadLocal<Connection> transaction = new ThreadLocal<>();
    private DataSource dataSource;

    public DefaultGetConnection(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection get() throws SQLException {
        if (FinalTransactionHolder.isOpen()) {
            return getSame();
        }
        return dataSource.getConnection();
    }

    public Connection getSame() throws SQLException {
        Connection connection = transaction.get();
        if (connection != null) {
            return connection;
        }
        connection = dataSource.getConnection();
        connection.setAutoCommit(false);// 事务
        transaction.set(connection);
        return connection;
    }

    public static Connection getConnection(){
        return transaction.get();
    }
}
