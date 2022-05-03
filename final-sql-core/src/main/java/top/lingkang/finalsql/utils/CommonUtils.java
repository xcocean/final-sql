package top.lingkang.finalsql.utils;

/**
 * @author lingkang
 * Created by 2022/5/3
 */
public class CommonUtils {
    public static boolean isEmpty(Object... args) {
        return args == null || args.length == 0;
    }

    public static boolean notEmpty(Object... args) {
        return args != null && args.length != 0;
    }
}
