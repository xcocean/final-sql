package top.lingkang.finalsql.dialect;

/**
 * @author lingkang
 * Created by 2022/4/11
 * 方言接口，使用方言处理以适应不同数据库ORM操作
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
     * @param row 类似添加mysql中的 limit 0,row
     * @return
     */
    String rowSql(String sql, int start, int row);

    /**
     * 用于分页统计
     */
    String total(String sql);
}
