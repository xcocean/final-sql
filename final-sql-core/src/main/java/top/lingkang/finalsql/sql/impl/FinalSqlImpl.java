package top.lingkang.finalsql.sql.impl;

import cn.hutool.core.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;
import top.lingkang.finalsql.annotation.Nullable;
import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.error.FinalException;
import top.lingkang.finalsql.sql.*;
import top.lingkang.finalsql.utils.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
public class FinalSqlImpl<T> implements FinalSql<T> {
    private SqlConfig sqlConfig;
    private static Logger log;

    private DataSource dataSource;
    private ResultHandler resultHandler;
    private SqlGenerate sqlGenerate;

    public FinalSqlImpl(SqlConfig config) {
        sqlConfig = config;
        this.dataSource = config.getDataSource();
        // ---------------  校验 ------------------
        if (sqlConfig == null)
            sqlConfig = new SqlConfig();
        // 配置
        if (sqlConfig.isShowSqlLog())
            log = LoggerFactory.getLogger(FinalSqlImpl.class);
        else
            log = NOPLogger.NOP_LOGGER;

        // ------------------- 实例化 --------------
        resultHandler = new ResultHandler(sqlConfig);
        sqlGenerate = new SqlGenerate(sqlConfig.getSqlDialect());
    }

    @Nullable
    @Override
    public List select(T entity) throws FinalException {
        return select(entity, null);
    }

    @Nullable
    @Override
    public List select(T entity, Condition condition) {
        Assert.notNull(entity, "查询对象不能为空！");
        Connection connection = getConnection();
        try {
            return resultHandler.list(execute(sqlGenerate.querySql(entity, condition), connection), entity);
        } catch (SQLException e) {
            throw new FinalException(e);
        } finally {
            DataSourceUtils.close(connection);
        }
    }

    @Nullable
    @Override
    public Object selectOne(Object entity) throws FinalException {
        return selectOne(entity, null);
    }

    @Nullable
    @Override
    public Object selectOne(Object entity, Condition condition) throws FinalException {
        Assert.notNull(entity, "查询对象不能为空！");
        Connection connection = getConnection();
        try {
            return resultHandler.one(execute(sqlGenerate.oneSql(entity, condition), connection), entity);
        } catch (SQLException e) {
            throw new FinalException(e);
        } finally {
            DataSourceUtils.close(connection);
        }
    }

    @Override
    public int selectCount(T entity) {
        return selectCount(entity, null);
    }

    @Override
    public int selectCount(T entity, Condition condition) {
        Assert.notNull(entity, "查询对象不能为空！");
        Connection connection = getConnection();
        try {
            return resultHandler.count(execute(sqlGenerate.countSql(entity, null), connection));
        } catch (SQLException e) {
            throw new FinalException(e);
        } finally {
            DataSourceUtils.close(connection);
        }
    }

    @Override
    public int insert(T entity) {
        Assert.notNull(entity, "插入对象不能为空！");
        Assert.isFalse(entity instanceof Class, "不能 insert " + entity.getClass());
        Connection connection = getConnection();
        try {
            return resultHandler.insert(executeInsert(sqlGenerate.insertSql(entity), connection), entity);
        } catch (SQLException | IllegalAccessException e) {
            throw new FinalException(e);
        } finally {
            DataSourceUtils.close(connection);
        }
    }

    @Override
    public int update(T entity) {
        return update(entity, null);
    }

    @Override
    public int update(T entity, Condition condition) {
        Assert.notNull(entity, "插入对象不能为空！");
        Assert.isFalse(entity instanceof Class, "不能 update 空对象");
        Connection connection = getConnection();
        try {
            return executeUpdate(sqlGenerate.updateSql(entity, condition), connection);
        } catch (SQLException e) {
            throw new FinalException(e);
        } finally {
            DataSourceUtils.close(connection);
        }
    }

    @Override
    public int delete(T entity) {
        return delete(entity, null);
    }

    @Override
    public int delete(T entity, Condition condition) {
        Assert.notNull(entity, "插入对象不能为空！");
        Assert.isFalse(entity instanceof Class, "不能 update 空对象");
        Connection connection = getConnection();
        try {
            return executeUpdate(sqlGenerate.deleteSql(entity, condition), connection);
        } catch (SQLException e) {
            throw new FinalException(e);
        } finally {
            DataSourceUtils.close(connection);
        }
    }

    // --------------------- 非接口操作  -----------------------------------------------------------------

    private ResultSet execute(ExSqlEntity exSqlEntity, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(exSqlEntity.getSql());
        // 设置参数
        statement.setFetchSize(sqlConfig.getFetchSize());
        statement.setMaxRows(sqlConfig.getMaxRows());
        try {
            for (int i = 0; i < exSqlEntity.getParam().size(); i++) {
                statement.setObject(i + 1, exSqlEntity.getParam().get(i));
            }
            log.info("\nsql: {}\nparam: {}", statement.toString(), exSqlEntity.getParam());
            return statement.executeQuery();
        } catch (SQLException e) {
            throw e;
        }
    }

    private ResultSet executeInsert(ExSqlEntity exSqlEntity, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(exSqlEntity.getSql(), PreparedStatement.RETURN_GENERATED_KEYS);
        // 设置参数
        statement.setFetchSize(sqlConfig.getFetchSize());
        statement.setMaxRows(sqlConfig.getMaxRows());
        try {
            for (int i = 0; i < exSqlEntity.getParam().size(); i++) {
                statement.setObject(i + 1, exSqlEntity.getParam().get(i));
            }
            log.info("\nsql: {}\nparam: {}", statement.toString(), exSqlEntity.getParam());
            statement.executeUpdate();
            return statement.getGeneratedKeys();
        } catch (SQLException e) {
            throw e;
        }
    }

    private int executeUpdate(ExSqlEntity exSqlEntity, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(exSqlEntity.getSql());
        // 设置参数
        statement.setFetchSize(sqlConfig.getFetchSize());
        statement.setMaxRows(sqlConfig.getMaxRows());
        try {
            for (int i = 0; i < exSqlEntity.getParam().size(); i++) {
                statement.setObject(i + 1, exSqlEntity.getParam().get(i));
            }
            log.info("\nsql: {}\nparam: {}", statement.toString(), exSqlEntity.getParam());
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
}
