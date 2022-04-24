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

    String rowSql(String sql, int row);
}
