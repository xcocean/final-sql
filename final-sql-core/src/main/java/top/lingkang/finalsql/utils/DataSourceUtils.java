package top.lingkang.finalsql.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lingkang.finalsql.constants.DbType;
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
    private static FinalSpringTransactionAutowired springTx = new FinalSpringTransactionAutowired() {
        @Override
        public void register() {
        }
    };

    public static Connection getConnection(DataSource dataSource) throws FinalException {
        Assert.notNull(dataSource, "未指定数 DataSource 据源");
        springTx.register();// spring的事务处理
        try {
            if (FinalTransactionHolder.isOpen()) {// 开启事务时需要获得同一个连接
                Connection connection = conn.get();
                if (connection == null) {
                    connection = dataSource.getConnection();
                    Assert.isFalse(connection.isClosed(), "DataSource 连接状态：close");
                    connection.setAutoCommit(false);
                    conn.set(connection);// 存储到当前线程
                }
                return connection;
            } else {
                return dataSource.getConnection();
            }
        } catch (Exception e) {
            throw new FinalException(e);
        }
    }

    public static Connection getConnection() throws FinalException {
        return conn.get();
    }

    public static void setSpringTx(FinalSpringTransactionAutowired springTx) {
        DataSourceUtils.springTx = springTx;
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


    public static DbType getDataType(DataSource dataSource) throws FinalException {
        Connection connection=null;
        try {
            connection = dataSource.getConnection();
            String name = connection.getMetaData().getDriverName();
            if (StrUtil.isEmpty(name)) {
                throw new FinalException("配置方言失败：未识别的jdbc连接驱动");
            }
            name = name.toLowerCase();
            if (name.indexOf("mysql") != -1) {
                return DbType.MYSQL;
            } else if (name.indexOf("postgresql") != -1) {
                return DbType.POSTGRESQL;
            }
        } catch (Exception e) {
            throw new FinalException(e);
        }finally {
            IoUtil.close(connection);
        }
        return DbType.OTHER;
    }


}
