package top.lingkang.finalsql.sql.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;
import top.lingkang.finalsql.annotation.Id;
import top.lingkang.finalsql.annotation.Nullable;
import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.constants.IdType;
import top.lingkang.finalsql.error.FinalException;
import top.lingkang.finalsql.sql.*;
import top.lingkang.finalsql.utils.ClassUtils;
import top.lingkang.finalsql.utils.DataSourceUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
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
            return resultHandler.count(execute(sqlGenerate.countSql(entity, condition), connection));
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

        // 检查id
        Field idField = ClassUtils.getIdField(entity.getClass().getDeclaredFields());
        if (idField==null){
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
        Connection connection = getConnection();
        ExSqlEntity exSqlEntity = sqlGenerate.insertSql(entity);
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(exSqlEntity.getSql(), Statement.RETURN_GENERATED_KEYS);
            // 设置参数
            statement.setFetchSize(sqlConfig.getFetchSize());
            statement.setMaxRows(sqlConfig.getMaxRows());
            for (int i = 0; i < exSqlEntity.getParam().size(); i++) {
                statement.setObject(i + 1, exSqlEntity.getParam().get(i));
            }
            log.info("\nsql: {}\nparam: {}", statement, exSqlEntity.getParam());
            statement.executeUpdate();
            return resultHandler.insert(statement.getGeneratedKeys(), entity);
        } catch (SQLException | IllegalAccessException e) {
            log.error("出现异常的SQL(请检查): \n\n{}\n\n", statement.toString());
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
        Assert.notNull(entity, "删除的对象不能为空！");
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
            log.error("出现异常的SQL(请检查): \n\n{}\n\n", statement.toString());
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
            log.error("出现异常的SQL(请检查): \n\n{}\n\n", statement.toString());
            throw e;
        } finally {
            IoUtil.close(statement);
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
            log.error("出现异常的SQL(请检查): \n\n{}\n\n", statement.toString());
            throw e;
        }
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
}
