package top.lingkang.finalsql.sql;

import top.lingkang.finalsql.error.FinalException;

/**
 * @author lingkang
 * Created by 2022/5/22
 */
public abstract class FinalPageHelper {
    public static final ThreadLocal<Boolean> IS_START = new ThreadLocal<>();
    public static final ThreadLocal<PageInfo> PAGE_INFO_THREAD_LOCAL = new ThreadLocal<>();

    public static void startPage(Integer page, Integer size) {
        IS_START.set(true);
        PAGE_INFO_THREAD_LOCAL.set(new PageInfo(page, size));
    }

    public static PageInfo getPageInfo() {
        PageInfo total = PAGE_INFO_THREAD_LOCAL.get();
        if (total == null)
            throw new FinalException("还未开启分页：FinalPageHelper.startPage();");
        return total;
    }
}
