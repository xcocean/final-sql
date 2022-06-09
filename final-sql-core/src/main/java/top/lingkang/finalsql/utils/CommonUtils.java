package top.lingkang.finalsql.utils;

import java.util.Collection;

/**
 * @author lingkang
 * Created by 2022/5/3
 */
public class CommonUtils {
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean notEmpty(Object... args) {
        return args != null && args.length != 0;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static void close(AutoCloseable closeable){
        if (closeable!=null){
            try {
                closeable.close();
            } catch (Exception e) {
                // 不做处理
            }
        }
    }
}
