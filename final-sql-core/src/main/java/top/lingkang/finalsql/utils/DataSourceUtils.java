package top.lingkang.finalsql.utils;

import cn.hutool.core.lang.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
public class DataSourceUtils {
    public static Connection getConnection(DataSource dataSource) {
        Assert.notNull(dataSource, "未指定数 DataSource 据源");
        try {
            Connection connection = dataSource.getConnection();
            Assert.notNull(connection, "DataSource 未指定连接 ");
            Assert.isFalse(connection.isClosed(), "DataSource 连接状态：close");
            return connection;
        } catch (SQLException e) {
            throw new IllegalArgumentException("获取 DataSource 连接异常：" + e.getMessage());
        }
    }
}
