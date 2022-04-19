package top.lingkang.finalsql.sql.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lingkang.finalsql.base.SqlInterceptor;
import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.sql.ExSqlEntity;
import top.lingkang.finalsql.sql.ResultCallback;
import top.lingkang.finalsql.utils.DataSourceUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author lingkang
 * Created by 2022/4/18
 * 执行相关抽象方法
 */
public abstract class AbstractFinalSqlExecute extends AbstractFinalConnection {
    protected SqlInterceptor interceptor;
    protected static Logger log;
    private static final Logger logger = LoggerFactory.getLogger(AbstractFinalSqlExecute.class);

    public AbstractFinalSqlExecute(SqlConfig sqlConfig) {
        super(sqlConfig);
    }

    protected <T> T execute(ExSqlEntity exSqlEntity, ResultCallback<T> rc) throws Exception {
        Connection connection = getConnection();
        interceptor.before(exSqlEntity);
        try {
            PreparedStatement statement = getPreparedStatement(connection, exSqlEntity.getSql(), exSqlEntity.getParam());

            log.info("\nsql: {}\nparam: {}", statement, exSqlEntity.getParam());
            T callback = rc.callback(statement.executeQuery());
            interceptor.after(exSqlEntity, callback);
            return callback;
        } catch (Exception e) {
            logger.error("出现异常的SQL(请检查): \n\n{}\n\n", exSqlEntity.toString());
            throw e;
        } finally {
            DataSourceUtils.close(connection);
        }
    }

    protected <T> int executeReturn(ExSqlEntity exSqlEntity, ResultCallback<T> rc) throws Exception {
        Connection connection = getConnection();
        interceptor.before(exSqlEntity);
        try {
            PreparedStatement statement = getPreparedStatementInsert(connection, exSqlEntity.getSql(), exSqlEntity.getParam());

            log.info("\nsql: {}\nparam: {}", statement, exSqlEntity.getParam());
            int success = statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            T callback = rc.callback(generatedKeys);
            interceptor.after(exSqlEntity, callback);
            return success;
        } catch (Exception e) {
            logger.error("出现异常的SQL(请检查): \n\n{}\n\n", exSqlEntity.toString());
            throw e;
        } finally {
            DataSourceUtils.close(connection);
        }
    }

    protected int executeUpdate(ExSqlEntity exSqlEntity) throws Exception {
        Connection connection = getConnection();
        interceptor.before(exSqlEntity);
        try {
            PreparedStatement statement = getPreparedStatement(connection, exSqlEntity.getSql(), exSqlEntity.getParam());

            log.info("\nsql: {}\nparam: {}", statement, exSqlEntity.getParam());
            int i = statement.executeUpdate();
            interceptor.after(exSqlEntity, i);
            return i;
        } catch (Exception e) {
            logger.error("出现异常的SQL(请检查): \n\n{}\n\n", exSqlEntity.toString());
            throw e;
        } finally {
            DataSourceUtils.close(connection);
        }
    }
}
