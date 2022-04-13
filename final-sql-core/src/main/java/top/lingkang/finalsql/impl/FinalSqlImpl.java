package top.lingkang.finalsql.impl;

import cn.hutool.core.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;
import top.lingkang.finalsql.FinalSql;
import top.lingkang.finalsql.ResultHandler;
import top.lingkang.finalsql.SqlConfig;
import top.lingkang.finalsql.error.FinalException;
import top.lingkang.finalsql.sql.ExSqlEntity;
import top.lingkang.finalsql.sql.SqlGenerate;
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

    @Override
    public List query(Object entity) throws FinalException {
        Assert.notNull(entity, "查询对象不能为空！");
        Connection connection = getConnection();
        try {
            return resultHandler.resultSetToList(execute(sqlGenerate.querySql(entity), connection), entity);
        } catch (SQLException e) {
            throw new FinalException(e);
        } finally {
            DataSourceUtils.close(connection);
        }
    }

    @Override
    public Object queryOne(Object entity) throws FinalException {
        Assert.notNull(entity, "查询对象不能为空！");
        Connection connection = getConnection();
        try {
            return resultHandler.resultSetToOne(execute(sqlGenerate.oneSql(entity), connection), entity);
        } catch (SQLException e) {
            throw new FinalException(e);
        } finally {
            DataSourceUtils.close(connection);
        }
    }

    @Override
    public int queryCount(T entity) {
        Assert.notNull(entity, "查询对象不能为空！");
        Connection connection = getConnection();
        try {
            return resultHandler.resultSetToCount(execute(sqlGenerate.countSql(entity), connection), entity);
        } catch (SQLException e) {
            throw new FinalException(e);
        } finally {
            DataSourceUtils.close(connection);
        }
    }

    private ResultSet execute(ExSqlEntity exSqlEntity, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(exSqlEntity.getSql());
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

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
}
