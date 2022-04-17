package top.lingkang.finalsql.transaction;

import top.lingkang.finalsql.error.TransactionException;
import top.lingkang.finalsql.utils.DataSourceUtils;

import java.sql.SQLException;

/**
 * @author lingkang
 * Created by 2022/4/13
 */
public abstract class FinalTransactionHolder {
    private static final ThreadLocal<Integer> isOpenTx = new ThreadLocal<>();

    public static boolean isOpen() {
        return isOpenTx.get() != null ? true : false;
    }

    /**
     * 开始事务
     */
    public static void begin() {
        isOpenTx.set(1);
    }

    /**
     * 提交事务
     */
    public static void commit() {
        if (!isOpen()) {
            throw new TransactionException("事务未开启！");
        }
        try {
            DataSourceUtils.getConnection().commit();
            isOpenTx.remove();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

    /**
     * 回滚事务
     */
    public static void rollback() {
        if (!isOpen()) {
            throw new TransactionException("事务未开启！");
        }
        try {
            DataSourceUtils.getConnection().rollback();
            isOpenTx.remove();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }
}
