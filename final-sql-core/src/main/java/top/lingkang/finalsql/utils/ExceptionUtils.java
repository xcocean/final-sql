package top.lingkang.finalsql.utils;

import org.slf4j.Logger;
import top.lingkang.finalsql.sql.ExSqlEntity;

/**
 * @author lingkang
 * Created by 2022/5/22
 */
public class ExceptionUtils {
    public static void outLogSql(ExSqlEntity exSqlEntity, Object result, Logger logger) {
        logger.info("\n┏━━━━━━━━━━━━━━━━━━━━━━ Final-sql ━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "┠ 位置: {}\n┠ SQL: {}\n┠ 参数: {}\n┠ 结果: {}\n" +
                        "┗━━━━━━━━━━━━━━━━━━━━━━ Final-sql ━━━━━━━━━━━━━━━━━━━━━━━",
                getUsePosition(),
                exSqlEntity.getSql(),
                exSqlEntity.getParam(),
                result
        );
    }

    public static void outError(ExSqlEntity exSqlEntity, Logger logger) {
        logger.info("\n┏━━━━━━━━━━━━━━━━━━━━━━ Final-sql ━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "┠ 出现异常的SQL(请检查):\n┠ 位置: {}\n┠ SQL: {}\n┠ 参数: {}\n" +
                        "┗━━━━━━━━━━━━━━━━━━━━━━ Final-sql ━━━━━━━━━━━━━━━━━━━━━━━",
                getUsePosition(),
                exSqlEntity.getSql(),
                exSqlEntity.getParam()
        );
    }

    private static StackTraceElement getUsePosition() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 5; i < 20 && i < stackTrace.length; i++) {
            String clazzName = stackTrace[i].getClassName();
            if (clazzName.contains("top.lingkang.finalsql.sql") ||
                    clazzName.contains("com.sun.proxy")) {
                continue;
            } else {
                return stackTrace[i];
            }
        }
        return null;
    }
}
