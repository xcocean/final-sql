package top.lingkang.finalsql.utils;

import java.util.Collection;

/**
 * @author lingkang
 * Created by 2022/6/9
 */
public final class AssertUtils {
    public static void notNull(Object obj, String msg) {
        if (obj == null) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void notEmpty(CharSequence charSequence, String msg) {
        if (charSequence == null || charSequence.length() == 0) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void notEmpty(Collection<?> charSequence, String msg) {
        if (charSequence == null || charSequence.isEmpty()) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void isFalse(boolean bl, String msg) {
        if (bl) {
            throw new IllegalArgumentException(msg);
        }
    }
}
