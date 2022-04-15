package top.lingkang.finalsql.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lingkang.finalsql.transaction.FinalTransactionHolder;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
public class DataSourceUtils {
    private static final Logger log = LoggerFactory.getLogger(DataSourceUtils.class);

    public static void close(AutoCloseable closeable) {
        if (FinalTransactionHolder.isOpen()) {
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
