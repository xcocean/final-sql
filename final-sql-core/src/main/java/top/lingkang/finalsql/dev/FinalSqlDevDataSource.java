package top.lingkang.finalsql.dev;

import top.lingkang.finalsql.error.FinalException;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * @author lingkang
 * Created by 2022/4/17
 * 开发专用dev，未实现连接池，每次调用均会初始化连接，不保证性能。
 * 用完注意关闭连接。
 */
public class FinalSqlDevDataSource implements DataSource {
    private String driver = "com.mysql.cj.jdbc.Driver",
            url = "jdbc:mysql://localhost:3306/test?serverTimezone=UTC",
            username = "root",
            password = "123456";

    public FinalSqlDevDataSource() {
    }

    public FinalSqlDevDataSource(String driver, String url, String username, String password) {
        this.driver = driver;
        this.username = username;
        this.password = password;
        this.url = url;
    }

    private Connection get() {
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(
                    url,
                    username,
                    password);
            if (conn.isClosed()) {
                throw new FinalException("无法连接到数据库：");
            }
            return conn;
        } catch (Exception e) {
            throw new FinalException(e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return get();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return get();
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
