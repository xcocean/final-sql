package top.lingkang.finalsql.sql.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;
import top.lingkang.finalsql.annotation.Nullable;
import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.constants.DbType;
import top.lingkang.finalsql.dialect.Mysql57Dialect;
import top.lingkang.finalsql.dialect.PostgreSqlDialect;
import top.lingkang.finalsql.dialect.SqlDialect;
import top.lingkang.finalsql.error.FinalException;
import top.lingkang.finalsql.error.ResultHandlerException;
import top.lingkang.finalsql.sql.*;
import top.lingkang.finalsql.utils.ClassUtils;
import top.lingkang.finalsql.utils.CommonUtils;
import top.lingkang.finalsql.utils.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

/**
 * @author lingkang
 * Created by 2022/4/11
 * 核心管理器
 */
public class FinalSqlManage extends AbstractFinalSqlExecute implements FinalSql {
    private SqlConfig sqlConfig;

    private ResultHandler resultHandler;

    public FinalSqlManage(SqlConfig config) {
        super(config);
        sqlConfig = config;
        this.dataSource = config.getDataSource();
        // ---------------  校验 ------------------
        if (sqlConfig == null)
            sqlConfig = new SqlConfig();
        // 配置
        if (sqlConfig.isShowSqlLog())
            logSql = LoggerFactory.getLogger(this.getClass());
        else
            logSql = NOPLogger.NOP_LOGGER;
        if (sqlConfig.isShowResultLog()) {
            logResult = LoggerFactory.getLogger(this.getClass());
        } else
            logResult = NOPLogger.NOP_LOGGER;
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
    public <T> List<T> select(Class<T> entity) {
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

    @Override
    public <T> List<T> select(Class<T> entity, Condition condition) {
        try {
            return select(entity.newInstance(), condition);
        } catch (Exception e) {
            throw new FinalException(e);
        }
    }

    @Nullable
    @Override
    public <T> T selectOne(T entity) {
        return selectOne(entity, null);
    }

    @Override
    public <T> T selectOne(Class<T> entity) {
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
            }, true);
        } catch (Exception e) {
            throw new FinalException(e);
        }
    }

    @Override
    public <T> T selectOne(Class<T> entity, Condition condition) {
        try {
            return selectOne(entity.newInstance(), condition);
        } catch (Exception e) {
            throw new FinalException(e);
        }
    }

    @Override
    public <T> int selectCount(T entity) {
        return selectCount(entity, null);
    }

    @Override
    public <T> int selectCount(Class<T> entity) {
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
            }, true);
        } catch (Exception e) {
            throw new FinalException(e);
        }
    }

    @Override
    public <T> int selectCount(Class<T> entity, Condition condition) {
        try {
            return selectCount(entity.newInstance(), condition);
        } catch (Exception e) {
            throw new FinalException(e);
        }
    }

    @Override
    public <T> List<T> selectForList(String sql, Class<T> t) {
        return selectForList(sql, t, new ArrayList());
    }

    @Override
    public <T> List<T> selectForListRow(String sql, Class<T> t, int row) {
        return selectForListRow(sql, t, row, new ArrayList());
    }

    @Override
    public <T> List<T> selectForList(String sql, Class<T> t, Object... param) {
        List<Object> ps = new ArrayList<>();
        if (CommonUtils.notEmpty(param)) {
            ps.addAll(Arrays.asList(param));
        }
        return selectForList(sql, t, ps);
    }

    @Override
    public <T> List<T> selectForList(String sql, Class<T> t, List param) {
        Assert.notNull(t, "查询的对象类型不能为空！");
        try {
            return execute(new ExSqlEntity(sql, param), new ResultCallback<List<T>>() {
                @Override
                public List<T> callback(ResultSet result) throws Exception {
                    return resultHandler.selectForList(result, t);
                }
            });
        } catch (Exception e) {
            if (e instanceof InstantiationException) {
                throw new FinalException("不支持的结果类型：" + t.getName());
            }
            throw new FinalException(e);
        }
    }

    @Override
    public <T> List<T> selectForListRow(String sql, Class<T> t, int row, Object... param) {
        Assert.notNull(t, "查询的对象类型不能为空！");
        List<Object> ps = new ArrayList<>();
        if (CommonUtils.notEmpty(param)) {
            ps.addAll(Arrays.asList(param));
        }
        return selectForListRow(sql, t, row, ps);
    }

    @Override
    public <T> List<T> selectForListRow(String sql, Class<T> t, int row, List param) {
        Assert.notNull(t, "查询的对象类型不能为空！");
        try {
            return execute(sqlGenerate.selectRowSql(new ExSqlEntity(sql, param), row), new ResultCallback<List<T>>() {
                @Override
                public List<T> callback(ResultSet result) throws Exception {
                    return resultHandler.selectForList(result, t);
                }
            });
        } catch (Exception e) {
            if (e instanceof InstantiationException) {
                throw new FinalException("不支持的结果类型：" + t.getName());
            }
            throw new FinalException(e);
        }
    }

    @Override
    public <T> T selectForObject(String sql, Class<T> t) {
        return selectForObject(sql, t, new ArrayList());
    }

    @Override
    public <T> T selectForObject(String sql, Class<T> t, Object... param) throws FinalException {
        List<Object> ps = new ArrayList<>();
        if (CommonUtils.notEmpty(param)) {
            ps.addAll(Arrays.asList(param));
        }
        return selectForObject(sql, t, ps);
    }

    @Override
    public <T> T selectForObject(String sql, Class<T> t, List param) {
        Assert.notNull(t, "查询的对象类型不能为空！");
        try {
            return execute(new ExSqlEntity(sql, param), new ResultCallback<T>() {
                @Override
                public T callback(ResultSet result) throws Exception {
                    return resultHandler.selectForObject(result, t);
                }
            }, true);
        } catch (Exception e) {
            throw new FinalException(e);
        }
    }

    @Override
    public Map selectForMap(String sql) {
        return selectForMap(sql, false, new ArrayList());
    }

    @Override
    public Map selectForMap(String sql, boolean isHump) {
        return selectForMap(sql, isHump, new ArrayList());
    }

    @Override
    public Map selectForMap(String sql, boolean isHump, Object... param) {
        List<Object> params = new ArrayList<>();
        if (CommonUtils.notEmpty(param))
            params = Arrays.asList(param);
        return selectForMap(sql, isHump, params);
    }

    @Override
    public Map selectForMap(String sql, boolean isHump, List param) {
        try {
            return execute(new ExSqlEntity(sql, param), new ResultCallback<Map>() {
                @Override
                public Map callback(ResultSet result) throws Exception {
                    if (result.next())
                        return resultHandler.selectForMap(result, isHump);
                    return null;
                }
            }, true);
        } catch (Exception e) {
            throw new FinalException(e);
        }
    }

    @Override
    public <T> int insert(T entity) {
        Assert.notNull(entity, "插入对象不能为空！");
        Assert.isFalse(entity instanceof Class, "不能 insert 类对象：" + entity.getClass());
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

        try {
            return executeReturn(sqlGenerate.batchInsert(entity), new ResultCallback<Integer>() {
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
        Assert.isFalse(entity instanceof Class, "不能 update 类对象：" + entity.getClass());
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
    public <T> int deleteByIds(Class<T> entity, Object... ids) {
        return deleteByIds(entity, Arrays.asList(ids));
    }

    @Override
    public <T> int deleteByIds(Class<T> entity, List ids) {
        Assert.notNull(entity, "删除的映射类不能为空！");
        Assert.notEmpty(ids, "入参 Id 不能为空！");
        try {
            return executeUpdate(sqlGenerate.deleteSql(entity, ids));
        } catch (Exception e) {
            throw new FinalException(e);
        }
    }

    @Override
    public <T> List<T> nativeSelect(String sql, ResultCallback<T> callback) throws FinalException {
        return nativeSelect(sql, callback, new ArrayList());
    }

    @Override
    public <T> List nativeSelect(String sql, ResultCallback<T> rc, Object... param) throws FinalException {
        List<Object> ps = new ArrayList<>();
        if (CommonUtils.notEmpty(param))
            ps = Arrays.asList(param);
        return nativeSelect(sql, rc, ps);
    }

    @Override
    public <T> List<T> nativeSelect(String sql, ResultCallback<T> callback, List param) throws FinalException {
        Assert.notEmpty(sql, "sql 不能为空！");
        try {
            return executeReturnList(new ExSqlEntity(sql, param), callback);
        } catch (Exception e) {
            throw new FinalException(e);
        }
    }

    @Override
    public int nativeUpdate(String sql) throws FinalException {
        return nativeUpdate(sql, new ArrayList());
    }

    @Override
    public int nativeUpdate(String sql, Object... param) throws FinalException {
        List<Object> params = new ArrayList<>();
        if (CommonUtils.notEmpty(param))
            params = Arrays.asList(param);
        return nativeUpdate(sql, params);
    }

    @Override
    public int nativeUpdate(String sql, List param) throws FinalException {
        Assert.notEmpty(sql, "sql 不能为空！");
        try {
            return executeUpdate(new ExSqlEntity(sql, param));
        } catch (Exception e) {
            throw new FinalException(e);
        }
    }

    @Override
    public DataSource getDataSource() {
        return super.getDataSource();
    }

    @Override
    public Connection getConnection() {
        return super.getConnection();
    }

    @Override
    public void begin() {
        super.begin();
    }

    @Override
    public void commit() {
        super.commit();
    }

    @Override
    public void rollback() {
        super.rollback();
    }

    // ----------------------------  mapper  相关 -------------------------------

    // cache
    private Map<String, Object> mappers = new HashMap<>();

    @Override
    public <T> T getMapper(Class<T> clazz) {
        Object has = mappers.get(clazz.getName());
        if (has != null) {
            return (T) has;
        }
        T mapper = ClassUtils.getMapper(clazz, new FinalMapperInvocation(clazz, this));
        mappers.put(clazz.getName(), mapper);
        return mapper;
    }

    // ------------------------------  初始化工作  ---------------------------------------------------
    private void checkDialect() {
        SqlDialect sqlDialect = sqlConfig.getSqlDialect();
        if (sqlDialect == null) {
            DbType dataType = DataSourceUtils.getDataType(dataSource);
            switch (dataType) {
                case MYSQL:
                    sqlConfig.setSqlDialect(new Mysql57Dialect());
                    break;
                case POSTGRESQL:
                    sqlConfig.setSqlDialect(new PostgreSqlDialect());
                    break;
                default:
                    throw new FinalException("未识别的jdbc连接驱动, 请自行实现 SqlDialect 进行配置方言");
            }
        }
    }
}
