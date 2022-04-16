package top.lingkang.finalsql.utils;

import cn.hutool.core.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lingkang.finalsql.error.FinalException;
import top.lingkang.finalsql.transaction.FinalSpringTransactionAutowired;
import top.lingkang.finalsql.transaction.FinalTransactionHolder;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
public class DataSourceUtils {
    private static final Logger log = LoggerFactory.getLogger(DataSourceUtils.class);
    private static final ThreadLocal<Connection> conn = new ThreadLocal<>();
    private static FinalSpringTransactionAutowired springTx=new PrivateFinalSpringTransactionAutowired();

    public static Connection getConnection(DataSource dataSource) throws FinalException {
        Assert.notNull(dataSource, "未指定数 DataSource 据源");
        springTx.register();// spring的事务处理
        try {
            if (FinalTransactionHolder.isOpen()) {// 开启事务时需要获得同一个连接
                Connection connection = conn.get();
                if (connection != null) {
                    Assert.isFalse(connection.isClosed(), "DataSource 连接状态：close");
                } else {
                    connection = dataSource.getConnection();
                    Assert.isFalse(connection.isClosed(), "DataSource 连接状态：close");
                    connection.setAutoCommit(false);
                    conn.set(connection);// 存储到当前线程
                }
                return connection;
            } else {
                Connection connection = dataSource.getConnection();
                Assert.isFalse(connection.isClosed(), "DataSource 连接状态：close");
                return connection;
            }
        } catch (Exception e) {
            throw new FinalException(e);
        }
    }

    public static Connection getConnection() throws FinalException {
        return conn.get();
    }

    public static void setSpringTx(FinalSpringTransactionAutowired springTx){
        DataSourceUtils.springTx=springTx;
    }

    public static void close(AutoCloseable closeable) {
        if (FinalTransactionHolder.isOpen()) {
            return;
        }
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                log.warn("关闭连接异常：", e);
            }
        }
    }


    static class PrivateFinalSpringTransactionAutowired implements FinalSpringTransactionAutowired{
        @Override
        public void register() {
        }
    }
}
