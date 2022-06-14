package top.lingkang.finalsql.example.sb.test.mysql;

import top.lingkang.finalsql.error.FinalException;

import java.sql.Connection;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class Test13Transaction extends TestBase {
    public static void main(String[] args) throws Exception {
        finalSql.beginTransaction();
        finalSql.beginTransaction();
        try {
            finalSql.nativeUpdate("update user set num=? where id=?",66,6);
            if (1==1)
                throw new RuntimeException("1");
        } catch (FinalException e) {
            finalSql.rollbackTransaction();
        }

        //设置当前事务的隔离级别
        finalSql.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    }
}
