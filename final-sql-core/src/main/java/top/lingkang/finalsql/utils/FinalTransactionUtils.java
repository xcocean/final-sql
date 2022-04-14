package top.lingkang.finalsql.utils;

import top.lingkang.finalsql.error.TransactionException;
import top.lingkang.finalsql.transaction.FinalTransaction;

import javax.sql.DataSource;
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

    public static void setDataSource(DataSource dataSource) {
        FinalTransaction transaction = ft.get();
        if (transaction == null) {
            throw new TransactionException("未开启事务！");
        }
        transaction.setDataSource(dataSource);
        transaction.setActivity(true);
        ft.set(transaction);
    }

    public static DataSource getDatasource() {
        FinalTransaction transaction = ft.get();
        if (transaction == null) {
            throw new TransactionException("未开启事务！");
        }
        return transaction.getDataSource();
    }

    public static void commit() throws SQLException {
        FinalTransaction transaction = ft.get();
        if (transaction == null) {
            throw new TransactionException("未开启事务！");
        }
        if (transaction.isActivity()){
            throw new TransactionException("事务未激活！");
        }
        try {
            transaction.getDataSource().getConnection().commit();
        } catch (SQLException e) {
            throw e;
        }finally {
            ft.remove();
        }
    }
}
