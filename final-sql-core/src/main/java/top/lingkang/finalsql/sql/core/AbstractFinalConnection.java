package top.lingkang.finalsql.sql.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.error.FinalSqlException;
import top.lingkang.finalsql.sql.SqlGenerate;
import top.lingkang.finalsql.utils.AssertUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/18
 * 数据库交互连接相关抽象方法
 */
public abstract class AbstractFinalConnection extends AbstractFinalCommonHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    protected DataSource dataSource;
    protected SqlGenerate sqlGenerate;
    protected final SqlConfig sqlConfig;
    private static final ThreadLocal<Connection> transaction = new ThreadLocal<>();

    public AbstractFinalConnection(SqlConfig sqlConfig) {
        this.sqlConfig = sqlConfig;
    }

    protected DataSource getDataSource() {
        return dataSource;
    }

    protected Connection getConnection() {
        Connection connection = transaction.get();
        if (connection != null) {
            return connection;
        }
        try {
            connection = dataSource.getConnection();
            AssertUtils.isFalse(connection.isClosed(), "DataSource 连接状态：close");
            return connection;
        } catch (SQLException e) {
            throw new FinalSqlException(e);
        }
    }

    protected void begin() {
        Connection connection = transaction.get();
        if (connection != null) {
            log.warn("事务已经是开启状态！");
            return;
        }
        try {
            connection = dataSource.getConnection();
            AssertUtils.isFalse(connection.isClosed(), "DataSource 连接状态：close");
            connection.setAutoCommit(false);// 开启事务
            transaction.set(connection);
        } catch (SQLException e) {
            throw new FinalSqlException(e);
        }
    }

    protected void commit() {
        Connection connection = transaction.get();
        if (connection == null)
            throw new FinalSqlException("事务未开启！");
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new FinalSqlException(e);
        } finally {
            ignoreTransactionClose(connection);
        }
    }

    protected void rollback() {
        Connection connection = transaction.get();
        if (connection == null)
            throw new FinalSqlException("事务未开启！");
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new FinalSqlException(e);
        } finally {
            ignoreTransactionClose(connection);
        }
    }

    protected boolean isOpenTransaction() {
        return transaction.get() != null;
    }

    protected void ignoreTransactionClose(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                transaction.remove();
                closeable.close();
            } catch (Exception e) {
                log.warn("关闭连接异常：", e);
            }
        }
    }

    protected void close(AutoCloseable closeable) {
        if (transaction.get() != null) {
            return;
        }
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                log.warn("关闭连接异常：", e);
            }
        }
    }

    // -----------------  数据库操作相关 -------------------------------

    protected PreparedStatement getPreparedStatement(Connection connection, String sql) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        applyStatementSettings(statement);
        return statement;
    }

    protected PreparedStatement getPreparedStatement(Connection connection, String sql, Object... param) throws SQLException {
        PreparedStatement statement = getPreparedStatement(connection, sql);
        applyStatementSettings(statement);
        // 设置参数
        setParamValue(statement, param);
        return statement;
    }

    protected PreparedStatement getPreparedStatement(Connection connection, String sql, List param) throws SQLException {
        PreparedStatement statement = getPreparedStatement(connection, sql);
        applyStatementSettings(statement);
        // 设置参数
        setParamValue(statement, param);
        return statement;
    }

    protected PreparedStatement getPreparedStatementInsert(Connection connection, String sql, List param) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        applyStatementSettings(statement);
        // 设置参数
        setParamValue(statement, param);
        return statement;
    }

    //  ---------------------------  辅助  ---------------------------------------------

    protected void setParamValue(PreparedStatement statement, List list) throws SQLException {
        for (int i = 0; i < list.size(); ) {
            Object o = list.get(i);
            if (o instanceof Date) {
                statement.setTimestamp(++i, new Timestamp(((Date) o).getTime()));
            } else {
                statement.setObject(++i, o);
            }
        }
    }

    protected void setParamValue(PreparedStatement statement, Object... param) throws SQLException {
        for (int i = 0; i < param.length; ) {
            Object o = param[i];
            if (o instanceof Date) {
                statement.setTimestamp(++i, new Timestamp(((Date) o).getTime()));
            } else {
                statement.setObject(++i, o);
            }
        }
    }

    protected void applyStatementSettings(PreparedStatement statement) throws SQLException {
        // 设置属性
        statement.setFetchSize(sqlConfig.getFetchSize());
        statement.setMaxRows(sqlConfig.getMaxRows());
    }
}
