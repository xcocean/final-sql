package top.lingkang.finalsql.sql;

import top.lingkang.finalsql.error.FinalException;

/**
 * @author lingkang
 * Created by 2022/5/22
 */
public abstract class FinalPageHelper {
    public static final ThreadLocal<Boolean> IS_START = new ThreadLocal<>();
    public static final ThreadLocal<PageInfo> PAGE_INFO_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 开始一个分页，执行FinalPageHelper.startPage 应该接着查询操作，非查询操作会导致异常
     *
     * @param page 开始页，从 1 开始
     * @param size 页长度
     */
    public static void startPage(Integer page, Integer size) {
        IS_START.set(true);
        PAGE_INFO_THREAD_LOCAL.set(new PageInfo(page, size));
    }

    /**
     * 获取分页数据，每个startPage对个一个getPageInfo，第二次getPageInfo将会抛出异常
     */
    public static PageInfo getPageInfo() {
        PageInfo total = PAGE_INFO_THREAD_LOCAL.get();
        if (total == null)
            throw new FinalException("还未开启分页：FinalPageHelper.startPage();");
        PAGE_INFO_THREAD_LOCAL.remove();
        return total;
    }
}
