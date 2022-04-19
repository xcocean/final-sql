package top.lingkang.finalsql.sql.core;

import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.error.FinalException;
import top.lingkang.finalsql.sql.SqlGenerate;
import top.lingkang.finalsql.utils.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/18
 * 数据库交互连接相关抽象方法
 */
public abstract class AbstractFinalConnection {
    protected DataSource dataSource;
    protected SqlGenerate sqlGenerate;
    private SqlConfig sqlConfig;

    public AbstractFinalConnection(SqlConfig sqlConfig) {
        this.sqlConfig = sqlConfig;
    }

    protected Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }

    protected Connection getConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    return connection;
                }
            } catch (SQLException e) {
                throw new FinalException(e);
            }
        }
        return getConnection();
    }

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
                statement.setDate(++i, new java.sql.Date(((Date) o).getTime()));
            } else {
                statement.setObject(++i, o);
            }
        }
    }

    protected void setParamValue(PreparedStatement statement, Object... param) throws SQLException {
        for (int i = 0; i < param.length; ) {
            Object o = param[i];
            if (o instanceof Date) {
                statement.setDate(++i, new java.sql.Date(((Date) o).getTime()));
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
