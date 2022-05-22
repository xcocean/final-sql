package top.lingkang.finalsql.dialect;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
public interface SqlDialect {
    String one(String sql);

    String count(String sql);

    String getTableName(String name);

    String nextval(String column);

    /**
     * 分页获取行
     * @param sql 类似添加mysql中的 limit
     * @param start 类似添加mysql中的 limit star,999
     * @param end 类似添加mysql中的 limit 0,end
     * @return
     */
    String rowSql(String sql, int start, int end);

    /**
     * 用于分页统计
     */
    String total(String sql);
}
