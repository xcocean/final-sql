package top.lingkang.finalsql.sql.impl;

import cn.hutool.core.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;
import top.lingkang.finalsql.annotation.Id;
import top.lingkang.finalsql.base.SqlInterceptor;
import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.constants.IdType;
import top.lingkang.finalsql.error.FinalException;
import top.lingkang.finalsql.error.ResultHandlerException;
import top.lingkang.finalsql.sql.*;
import top.lingkang.finalsql.utils.ClassUtils;
import top.lingkang.finalsql.utils.DataSourceUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
public class FinalSqlImpl implements FinalSql {
    private SqlConfig sqlConfig;
    private static Logger log;
    private static final Logger logger = LoggerFactory.getLogger(FinalSqlImpl.class);

    private DataSource dataSource;
    private ResultHandler resultHandler;
    private SqlGenerate sqlGenerate;
    private SqlInterceptor interceptor;

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
        interceptor = sqlConfig.getInterceptor();
    }

    @Override
    public <T> List<T> select(T entity) {
        return select(entity, null);
    }

    @Override
    public <T> List<T> select(T entity, Condition condition) {
        Assert.notNull(entity, "查询对象不能为空！");
        try {
            return execute(sqlGenerate.querySql(entity, condition), new ResultCallback<List<T>>() {
                @Override
                public List<T> callback(ResultSet result) throws SQLException {
                    List<T> list = resultHandler.list(result, entity);
                    return list;
                }
            });
        } catch (SQLException e) {
            throw new FinalException(e);
        }
    }

    @Override
    public <T> T selectOne(T entity) {
        return selectOne(entity, null);
    }

    @Override
    public <T> T selectOne(T entity, Condition condition) {
        Assert.notNull(entity, "查询对象不能为空！");
        try {
            return execute(sqlGenerate.oneSql(entity, condition), new ResultCallback<T>() {
                @Override
                public T callback(ResultSet result) throws SQLException {
                    return resultHandler.one(result, entity);
                }
            });
        } catch (SQLException e) {
            throw new FinalException(e);
        }
    }

    @Override
    public <T> int selectCount(T entity) {
        return selectCount(entity, null);
    }

    @Override
    public <T> int selectCount(T entity, Condition condition) {
        Assert.notNull(entity, "查询对象不能为空！");
        try {
            return execute(sqlGenerate.countSql(entity, condition), new ResultCallback<Integer>() {
                @Override
                public Integer callback(ResultSet result) throws SQLException {
                    if (result.next()) {
                        return result.getInt(1);
                    }
                    return 0;
                }
            });
        } catch (SQLException e) {
            throw new FinalException(e);
        }
    }

    @Override
    public <T> int insert(T entity) {
        Assert.notNull(entity, "插入对象不能为空！");
        Assert.isFalse(entity instanceof Class, "不能 insert " + entity.getClass());

        // 检查id
        Field idField = ClassUtils.getIdField(entity.getClass().getDeclaredFields());
        if (idField == null) {
            throw new FinalException("实体对象未添加 @Id 注解！");
        }
        Id id = idField.getAnnotation(Id.class);
        if (id.value() == IdType.AUTO) {
            idField.setAccessible(true);
            try {
                idField.set(entity, null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (id.value() == IdType.INPUT) {
            if (ClassUtils.getValue(entity, entity.getClass(), idField.getName()) == null) {
                throw new FinalException("实体对象 @Id 类型为 IdType.INPUT，则主键 id 的值不能为空！");
            }
        }

        try {
            return executeReturn(sqlGenerate.insertSql(entity), new ResultCallback<Integer>() {
                @Override
                public Integer callback(ResultSet result) throws SQLException {
                    try {
                        return resultHandler.insert(result, entity);
                    } catch (IllegalAccessException e) {
                        throw new ResultHandlerException(e);
                    }
                }
            });
        } catch (SQLException e) {
            throw new ResultHandlerException(e);
        }
    }

    @Override
    public <T> int update(T entity) {
        return update(entity, null);
    }

    @Override
    public <T> int update(T entity, Condition condition) {
        Assert.notNull(entity, "插入对象不能为空！");
        Assert.isFalse(entity instanceof Class, "不能 update 空对象");
        try {
            return executeUpdate(sqlGenerate.updateSql(entity, condition));
        } catch (SQLException e) {
            throw new FinalException(e);
        }
    }

    @Override
    public <T> int delete(T entity) {
        return delete(entity, null);
    }

    @Override
    public <T> int delete(T entity, Condition condition) {
        Assert.notNull(entity, "删除的对象不能为空！");
        try {
            return executeUpdate(sqlGenerate.deleteSql(entity, condition));
        } catch (SQLException e) {
            throw new FinalException(e);
        }
    }

    @Override
    public <T> List<T> nativeSelect(String sql, ResultCallback<T> callback) throws FinalException {
        return nativeSelect(sql, callback, null);
    }

    @Override
    public <T> List nativeSelect(String sql, ResultCallback<T> rc, Object... param) throws FinalException {
        Assert.notEmpty(sql, "sql 不能为空！");
        Connection connection = getConnection();
        try {
            PreparedStatement statement = null;
            if (param == null) {
                statement = getPreparedStatement(connection, sql);
            } else {
                statement = getPreparedStatement(connection, sql, param);
            }
            ResultSet resultSet = statement.executeQuery();
            List list = new ArrayList();
            while (resultSet.next())
                list.add(rc.callback(resultSet));
            return list;
        } catch (SQLException e) {
            throw new FinalException(e);
        } finally {
            DataSourceUtils.close(connection);
        }
    }

    // --------------------- 非接口操作  -----------------------------------------------------------------

    private <T> T execute(ExSqlEntity exSqlEntity, ResultCallback<T> rc) throws SQLException {
        Connection connection = getConnection();
        interceptor.before(exSqlEntity);
        try {
            PreparedStatement statement = getPreparedStatement(connection, exSqlEntity.getSql(), exSqlEntity.getParam());
            applyStatementSettings(statement);
            log.info("\nsql: {}\nparam: {}", statement, exSqlEntity.getParam());
            T callback = rc.callback(statement.executeQuery());
            interceptor.after(exSqlEntity, callback);
            return callback;
        } catch (SQLException e) {
            logger.error("出现异常的SQL(请检查): \n\n{}\n\n", exSqlEntity.toString());
            throw e;
        } finally {
            DataSourceUtils.close(connection);
        }
    }

    private <T> int executeReturn(ExSqlEntity exSqlEntity, ResultCallback<T> rc) throws SQLException {
        Connection connection = getConnection();
        interceptor.before(exSqlEntity);
        try {
            PreparedStatement statement = getPreparedStatement(connection, exSqlEntity.getSql(), exSqlEntity.getParam());
            applyStatementSettings(statement);

            log.info("\nsql: {}\nparam: {}", statement, exSqlEntity.getParam());
            int success = statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            T callback = rc.callback(generatedKeys);
            interceptor.after(exSqlEntity, callback);
            return success;
        } catch (SQLException e) {
            logger.error("出现异常的SQL(请检查): \n\n{}\n\n", exSqlEntity.toString());
            throw e;
        } finally {
            DataSourceUtils.close(connection);
        }
    }

    private int executeUpdate(ExSqlEntity exSqlEntity) throws SQLException {
        Connection connection = getConnection();
        interceptor.before(exSqlEntity);
        try {
            PreparedStatement statement = getPreparedStatement(connection, exSqlEntity.getSql(), exSqlEntity.getParam());
            log.info("\nsql: {}\nparam: {}", statement, exSqlEntity.getParam());
            int i = statement.executeUpdate();
            interceptor.after(exSqlEntity, i);
            return i;
        } catch (SQLException e) {
            logger.error("出现异常的SQL(请检查): \n\n{}\n\n", exSqlEntity.toString());
            throw e;
        } finally {
            DataSourceUtils.close(connection);
        }
    }


    // ----------------------  连接获取、数据库操作获取、参数统一设置  -------------------------------------------------------------------

    private PreparedStatement getPreparedStatement(Connection connection, String sql) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        // 设置属性
        statement.setFetchSize(sqlConfig.getFetchSize());
        statement.setMaxRows(sqlConfig.getMaxRows());
        return statement;
    }

    private PreparedStatement getPreparedStatement(Connection connection, String sql, Object... param) throws SQLException {
        PreparedStatement statement = getPreparedStatement(connection, sql);
        // 设置参数
        for (int i = 0; i < param.length; i++) {
            statement.setObject(i + 1, param[i]);
        }
        return statement;
    }

    private PreparedStatement getPreparedStatement(Connection connection, String sql, List param) throws SQLException {
        PreparedStatement statement = getPreparedStatement(connection, sql);
        // 设置参数
        for (int i = 0; i < param.size(); i++) {
            statement.setObject(i + 1, param.get(i));
        }
        return statement;
    }

    private PreparedStatement getPreparedStatementInsert(Connection connection, String sql, List param) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        // 设置参数
        for (int i = 0; i < param.size(); i++) {
            statement.setObject(i + 1, param.get(i));
        }
        return statement;
    }

    private void applyStatementSettings(PreparedStatement statement) throws SQLException {
        // 设置属性
        statement.setFetchSize(sqlConfig.getFetchSize());
        statement.setMaxRows(sqlConfig.getMaxRows());
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
}
