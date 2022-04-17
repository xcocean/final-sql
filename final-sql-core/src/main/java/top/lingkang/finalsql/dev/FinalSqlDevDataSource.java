package top.lingkang.finalsql.dev;

import top.lingkang.finalsql.error.FinalException;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * @author lingkang
 * Created by 2022/4/17
 */
public class FinalSqlDevDataSource implements DataSource {
    private Connection connection;

    public FinalSqlDevDataSource(Connection connection) {
        if (connection==null){
            throw new FinalException("Connection 不能为空！");
        }
        try {
            if (connection.isClosed()){
                throw new FinalException("Connection 为关闭状态！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.connection = connection;

    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return connection;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
