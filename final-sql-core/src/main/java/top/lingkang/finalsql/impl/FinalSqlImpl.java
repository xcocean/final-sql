package top.lingkang.finalsql.impl;

import cn.hutool.core.io.IoUtil;
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
        if (sqlConfig == null)
            sqlConfig = new SqlConfig();
        // 配置
        if (sqlConfig.isShowSqlLog())
            log = LoggerFactory.getLogger(FinalSqlImpl.class);
        else
            log = NOPLogger.NOP_LOGGER;

        resultHandler = new ResultHandler(sqlConfig);
        sqlGenerate = new SqlGenerate(sqlConfig);
    }

    @Override
    public List query(Object entity)  throws FinalException{
        Assert.notNull(entity, "查询对象不能为空！");
        Connection connection = getConnection();
        try {
            return resultHandler.resultSetToList(exeResultSet(sqlGenerate.querySql(entity)), entity);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
           IoUtil.close(connection);
        }
        return null;
    }

    @Override
    public Object queryOne(Object entity) throws FinalException {
        Assert.notNull(entity, "查询对象不能为空！");
        Connection connection = getConnection();
        try {
            return resultHandler.resultSetToOne(exeResultSet(sqlGenerate.oneSql(entity)), entity);
        } catch (SQLException e) {
            throw new FinalException(e);
        } finally {
            IoUtil.close(connection);
        }
    }

    private ResultSet exeResultSet(ExSqlEntity exSqlEntity) throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement(exSqlEntity.getSql());
        for (int i = 0; i < exSqlEntity.getParam().size(); i++) {
            preparedStatement.setObject(i + 1, exSqlEntity.getParam().get(i));
        }
        log.info("\nsql: {}\nparam: {}", preparedStatement.toString(), exSqlEntity.getParam());
        return preparedStatement.executeQuery();
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
}
