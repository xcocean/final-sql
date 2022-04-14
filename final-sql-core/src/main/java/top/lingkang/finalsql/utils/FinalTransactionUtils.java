package top.lingkang.finalsql.utils;

import top.lingkang.finalsql.error.TransactionException;
import top.lingkang.finalsql.transaction.FinalTransaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lingkang
 * Created by 2022/4/14
 */
public class FinalTransactionUtils {
    private static final ThreadLocal<FinalTransaction> ft = new ThreadLocal<>();

    public static void begin() {
        if (ft.get() != null) {
            throw new TransactionException("是已经处于开启状态！");
        }
        ft.set(new FinalTransaction());
    }

    public static FinalTransaction getFinalTransaction() {
        return ft.get();
    }

    public static void setConnection(Connection connection) {
        FinalTransaction transaction = ft.get();
        if (transaction == null) {
            throw new TransactionException("未开启事务！");
        }
        transaction.setConnection(connection);
        transaction.setActivity(true);
        ft.set(transaction);
    }

    public static Connection getConnection() {
        FinalTransaction transaction = ft.get();
        if (transaction == null) {
            throw new TransactionException("未开启事务！");
        }
        return transaction.getConnection();
    }

    public static void commit() throws SQLException {
        FinalTransaction transaction = ft.get();
        if (transaction == null) {
            throw new TransactionException("未开启事务！");
        }
        if (!transaction.isActivity()) {
            throw new TransactionException("事务未激活！");
        }
        try {
            transaction.getConnection().commit();
        } catch (SQLException e) {
            throw e;
        } finally {
            transaction.getConnection().close();
            ft.remove();
        }
    }

    public static void rollback() throws SQLException {
        FinalTransaction transaction = ft.get();
        if (transaction == null) {
            throw new TransactionException("未开启事务！");
        }
        if (!transaction.isActivity()) {
            throw new TransactionException("事务未激活！");
        }
        try {
            transaction.getConnection().rollback();
        } catch (SQLException e) {
            throw e;
        } finally {
            transaction.getConnection().close();
            ft.remove();
        }
    }
}
