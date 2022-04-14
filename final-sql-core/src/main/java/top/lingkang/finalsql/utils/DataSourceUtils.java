package top.lingkang.finalsql.utils;

import cn.hutool.core.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lingkang.finalsql.error.ConnectionException;
import top.lingkang.finalsql.transaction.FinalTransaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
public class DataSourceUtils {
    private static final Logger log = LoggerFactory.getLogger(DataSourceUtils.class);

    public static Connection getConnection(DataSource dataSource) {
        Assert.notNull(dataSource, "未指定数 DataSource 据源");
        try {
            FinalTransaction transaction = FinalTransactionUtils.getFinalTransaction();
            if (transaction != null) {
                if (transaction.isActivity()) {
                    return transaction.getDataSource().getConnection();
                } else {
                    Connection connection = dataSource.getConnection();
                    Assert.notNull(connection, "DataSource 未指定连接 ");
                    Assert.isFalse(connection.isClosed(), "DataSource 连接状态：close");
                    connection.setAutoCommit(true);
                    FinalTransactionUtils.setDataSource(dataSource);
                    return connection;
                }
            }
            Connection connection = dataSource.getConnection();
            Assert.notNull(connection, "DataSource 未指定连接 ");
            Assert.isFalse(connection.isClosed(), "DataSource 连接状态：close");
            return connection;
        } catch (SQLException e) {
            throw new ConnectionException(e);
        }
    }

    public static void close(AutoCloseable closeable) {
        if (FinalTransactionUtils.getFinalTransaction() != null) {
            return;
        }
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                log.error("关闭连接异常：", e);
            }
        }
    }
}
