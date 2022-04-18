package top.lingkang.finalsql.sql.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;
import top.lingkang.finalsql.annotation.Id;
import top.lingkang.finalsql.annotation.Nullable;
import top.lingkang.finalsql.base.SqlInterceptor;
import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.constants.IdType;
import top.lingkang.finalsql.dialect.Mysql57Dialect;
import top.lingkang.finalsql.dialect.PostgreSqlDialect;
import top.lingkang.finalsql.dialect.SqlDialect;
import top.lingkang.finalsql.error.FinalException;
import top.lingkang.finalsql.error.ResultHandlerException;
import top.lingkang.finalsql.sql.*;
import top.lingkang.finalsql.utils.ClassUtils;
import top.lingkang.finalsql.utils.DataSourceUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
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
        this.checkDialect();
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
                public List<T> callback(ResultSet result) throws Exception {
                    List<T> list = resultHandler.list(result, entity);
                    return list;
                }
            });
        } catch (Exception e) {
            throw new FinalException(e);
        }
    }

    @Nullable
    @Override
    public <T> T selectOne(T entity) {
        return selectOne(entity, null);
    }

    @Nullable
    @Override
    public <T> T selectOne(T entity, Condition condition) {
        Assert.notNull(entity, "查询对象不能为空！");
        try {
            return execute(sqlGenerate.oneSql(entity, condition), new ResultCallback<T>() {
                @Override
                public T callback(ResultSet result) throws Exception {
                    if (result.next()) {
                        return resultHandler.one(result, entity);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
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
                public Integer callback(ResultSet result) throws Exception {
                    if (result.next()) {
                        return result.getInt(1);
                    }
                    return 0;
                }
            });
        } catch (Exception e) {
            throw new FinalException(e);
        }
    }

    @Override
    public <T> int insert(T entity) {
        Assert.notNull(entity, "插入对象不能为空！");
        Assert.isFalse(entity instanceof Class, "不能 insert " + entity.getClass());

        // 检查id
        this.checkId(entity);

        try {
            return executeReturn(sqlGenerate.insertSql(entity), new ResultCallback<Integer>() {
                @Override
                public Integer callback(ResultSet result) throws Exception {
                    try {
                        if (result.next())
                            return resultHandler.insert(result, entity);
                        return 0;
                    } catch (IllegalAccessException e) {
                        throw new ResultHandlerException(e);
                    }
                }
            });
        } catch (Exception e) {
            throw new FinalException(e);
        }
    }

    @Override
    public <T> int batchInsert(List<T> entity) throws FinalException {
        if (CollUtil.isEmpty(entity)) {
            throw new FinalException("批量插入不能为空！");
        } else if (entity.size() > 200) {
            throw new FinalException("每次批量插入不能大于 200 条！");
        }

        String sql = "";
        List<Object> param = new ArrayList<>();
        boolean isFirst = false;
        int start = 0, eq = entity.size() - 1;
        for (int i = 0; i < entity.size(); i++) {
            T t = entity.get(i);
            Assert.isFalse(t instanceof Class, "不能 insert " + entity.getClass());
            this.checkId(t);
            ExSqlEntity exSqlEntity = sqlGenerate.insertSql(t);
            if (!isFirst) {
                sql += exSqlEntity.getSql() + "";
                start = sql.indexOf("values") + 7;
                isFirst = true;
            } else {
                sql += exSqlEntity.getSql().substring(start);
            }
            if (i == eq) {
                sql += ";";
            } else {
                sql += ",\n";
            }
            param.addAll(exSqlEntity.getParam());
        }

        try {
            return executeReturn(new ExSqlEntity(sql, param), new ResultCallback<Integer>() {
                @Override
                public Integer callback(ResultSet result) throws Exception {
                    try {
                       return resultHandler.batchInsert(result, entity);
                    } catch (IllegalAccessException e) {
                        throw new ResultHandlerException(e);
                    }
                }
            });
        } catch (Exception e) {
            throw new FinalException(e);
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            throw new FinalException(e);
        } finally {
            DataSourceUtils.close(connection);
        }
    }

    // --------------------- 非接口操作  -----------------------------------------------------------------

    private <T> T execute(ExSqlEntity exSqlEntity, ResultCallback<T> rc) throws Exception {
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

    private <T> int executeReturn(ExSqlEntity exSqlEntity, ResultCallback<T> rc) throws Exception {
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

    private int executeUpdate(ExSqlEntity exSqlEntity) throws Exception {
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


    // ----------------------  连接获取、数据库操作获取、参数统一设置  -------------------------------------------------------------------

    private PreparedStatement getPreparedStatement(Connection connection, String sql) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        applyStatementSettings(statement);
        return statement;
    }

    private PreparedStatement getPreparedStatement(Connection connection, String sql, Object... param) throws SQLException {
        PreparedStatement statement = getPreparedStatement(connection, sql);
        applyStatementSettings(statement);
        // 设置参数
        setParamValue(statement, param);
        return statement;
    }

    private PreparedStatement getPreparedStatement(Connection connection, String sql, List param) throws SQLException {
        PreparedStatement statement = getPreparedStatement(connection, sql);
        applyStatementSettings(statement);
        // 设置参数
        setParamValue(statement, param);
        return statement;
    }

    private PreparedStatement getPreparedStatementInsert(Connection connection, String sql, List param) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        applyStatementSettings(statement);
        // 设置参数
        setParamValue(statement, param);
        return statement;
    }

    private void setParamValue(PreparedStatement statement, List list) throws SQLException {
        for (int i = 0; i < list.size(); ) {
            Object o = list.get(i);
            if (o instanceof Date) {
                statement.setDate(++i, new java.sql.Date(((Date) o).getTime()));
            } else {
                statement.setObject(++i, o);
            }
        }
    }

    private void setParamValue(PreparedStatement statement, Object... param) throws SQLException {
        for (int i = 0; i < param.length; ) {
            Object o = param[i];
            if (o instanceof Date) {
                statement.setDate(++i, new java.sql.Date(((Date) o).getTime()));
            } else {
                statement.setObject(++i, o);
            }
        }
    }

    private void applyStatementSettings(PreparedStatement statement) throws SQLException {
        // 设置属性
        statement.setFetchSize(sqlConfig.getFetchSize());
        statement.setMaxRows(sqlConfig.getMaxRows());
    }

    private <T> void checkId(T entity) {
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
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }

    // ------------------------------  初始化工作  ---------------------------------------------------
    private void checkDialect() {
        SqlDialect sqlDialect = sqlConfig.getSqlDialect();
        if (sqlDialect == null) {
            try {
                Connection connection = getConnection();
                String name = connection.getMetaData().getDriverName();
                if (StrUtil.isEmpty(name)) {
                    throw new FinalException("配置方言失败：未识别的jdbc连接驱动");
                }
                name = name.toLowerCase();
                logger.info("final-sql: loading {}", name);
                if (name.indexOf("mysql") != -1) {
                    sqlConfig.setSqlDialect(new Mysql57Dialect());
                } else if (name.indexOf("postgresql") != -1) {
                    sqlConfig.setSqlDialect(new PostgreSqlDialect());
                } else {
                    throw new FinalException("未识别的jdbc连接驱动, 请自行实现 SqlDialect 进行配置方言");
                }
                IoUtil.close(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
