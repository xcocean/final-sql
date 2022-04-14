package top.lingkang.finalsql.transaction;

import top.lingkang.finalsql.error.TransactionException;
import top.lingkang.finalsql.utils.FinalTransactionUtils;

import java.sql.SQLException;

/**
 * @author lingkang
 * Created by 2022/4/13
 */
public abstract class FinalTransactionHolder {

    /**
     * 开始事务
     */
    public static void begin() {
        FinalTransactionUtils.begin();

    }

    /**
     * 提交事务
     */
    public static void commit() {
        try {
            FinalTransactionUtils.commit();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }
}
