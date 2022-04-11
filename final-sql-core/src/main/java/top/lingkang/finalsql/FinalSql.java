package top.lingkang.finalsql;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;
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
public class FinalSql {
    private SqlConfig sqlConfig;
    private static Logger log;

    private DataSource dataSource;
    private ResultHandler resultHandler;

    public FinalSql(DataSource dataSource) {
        this(dataSource, null);
    }

    public FinalSql(DataSource dataSource, SqlConfig config) {
        sqlConfig = config;
        this.dataSource = dataSource;
        if (sqlConfig == null)
            sqlConfig = new SqlConfig();
        // 配置
        if (sqlConfig.isShowSqlLog())
            log = LoggerFactory.getLogger(FinalSql.class);
        else
            log = NOPLogger.NOP_LOGGER;

        resultHandler = new ResultHandler(sqlConfig);
    }

    public <T> List query(T t) {
        Assert.notNull(t, "查询对象不能为空！");
        Connection connection = getConnection();
        try {
            String sql = SqlGenerate.entitySql(t);
            // sql += " where id=444";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            log.info(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Object> list = resultHandler.resultSetToList(resultSet, t);
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(connection);
        }
        return null;
    }


    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
}
