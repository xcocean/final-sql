package top.lingkang.finalsql.sql.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lingkang.finalsql.base.SqlInterceptor;
import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.sql.ExSqlEntity;
import top.lingkang.finalsql.sql.ResultCallback;
import top.lingkang.finalsql.utils.ExceptionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/18
 * 执行相关抽象方法
 */
public abstract class AbstractFinalSqlExecute extends AbstractFinalConnection {
    public static SqlInterceptor[] interceptor;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AbstractFinalSqlExecute(SqlConfig sqlConfig) {
        super(sqlConfig);
    }

    protected <T> T execute(ExSqlEntity exSqlEntity, ResultCallback<T> rc) throws Exception {
        return execute(exSqlEntity, rc, false);
    }

    protected <T> T execute(ExSqlEntity exSqlEntity, ResultCallback<T> rc, boolean oneRow) throws Exception {
        Connection connection = getConnection();
        before(exSqlEntity, connection);
        try {
            PreparedStatement statement = getPreparedStatement(connection, exSqlEntity.getSql(), exSqlEntity.getParam());
            if (oneRow)
                statement.setMaxRows(1);
            T callback = rc.callback(statement.executeQuery());
            after(exSqlEntity, connection, callback);
            if (sqlConfig.isShowLog())
                ExceptionUtils.outLogSql(exSqlEntity, callback, logger);
            statement.close();
            return callback;
        } catch (Exception e) {
            ExceptionUtils.outError(exSqlEntity, logger);
            throw e;
        } finally {
            close(connection);
        }
    }

    protected <T> int executeReturn(ExSqlEntity exSqlEntity, ResultCallback<T> rc) throws Exception {
        Connection connection = getConnection();
        before(exSqlEntity, connection);
        try {
            PreparedStatement statement = getPreparedStatementInsert(connection, exSqlEntity.getSql(), exSqlEntity.getParam());

            int success = statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            T callback = rc.callback(generatedKeys);
            after(exSqlEntity, connection, callback);
            if (sqlConfig.isShowLog())
                ExceptionUtils.outLogSql(exSqlEntity, callback, logger);
            statement.close();
            return success;
        } catch (Exception e) {
            ExceptionUtils.outError(exSqlEntity, logger);
            throw e;
        } finally {
            close(connection);
        }
    }

    protected <T> List<T> executeReturnList(ExSqlEntity exSqlEntity, ResultCallback<T> rc) throws Exception {
        Connection connection = getConnection();
        before(exSqlEntity, connection);
        try {
            PreparedStatement statement = getPreparedStatement(connection, exSqlEntity.getSql(), exSqlEntity.getParam());

            ResultSet resultSet = statement.executeQuery();
            List<T> callback = new ArrayList<>();
            while (resultSet.next()) {
                callback.add(rc.callback(resultSet));
            }
            after(exSqlEntity, connection, callback);
            if (sqlConfig.isShowLog())
                ExceptionUtils.outLogSql(exSqlEntity, callback, logger);
            statement.close();
            return callback;
        } catch (Exception e) {
            ExceptionUtils.outError(exSqlEntity, logger);
            throw e;
        } finally {
            close(connection);
        }
    }

    protected int executeUpdate(ExSqlEntity exSqlEntity) throws Exception {
        Connection connection = getConnection();
        before(exSqlEntity, connection);
        try {
            PreparedStatement statement = getPreparedStatement(connection, exSqlEntity.getSql(), exSqlEntity.getParam());

            int i = statement.executeUpdate();
            after(exSqlEntity, connection, i);
            if (sqlConfig.isShowLog())
                ExceptionUtils.outLogSql(exSqlEntity, i, logger);
            statement.close();
            return i;
        } catch (Exception e) {
            ExceptionUtils.outError(exSqlEntity, logger);
            throw e;
        } finally {
            close(connection);
        }
    }

    private void before(ExSqlEntity exSqlEntity, Connection connection) {
        if (interceptor != null)
            for (SqlInterceptor in : interceptor) {
                in.before(exSqlEntity, connection);
            }
    }

    private void after(ExSqlEntity sqlEntity, Connection connection, Object result) {
        if (interceptor != null)
            for (SqlInterceptor in : interceptor) {
                in.after(sqlEntity, connection, result);
            }
    }
}
