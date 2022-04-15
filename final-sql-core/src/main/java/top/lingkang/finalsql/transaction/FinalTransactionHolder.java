package top.lingkang.finalsql.transaction;

import top.lingkang.finalsql.error.TransactionException;
import top.lingkang.finalsql.sql.conn.DefaultGetConnection;

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
        try {
            DefaultGetConnection.getConnection().commit();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

    public static void rollback() {
        try {
            DefaultGetConnection.getConnection().rollback();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }


}
