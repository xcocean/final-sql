package top.lingkang.finalsql.dialect;

/**
 * @author lingkang
 * Created by 2022/4/18
 */
public class PostgreSqlDialect implements SqlDialect {
    @Override
    public String one(String sql) {
        return "select " + sql + " limit 1";
    }

    @Override
    public String count(String sql) {
        return "select count(*) " + sql;
    }

    @Override
    public String getTableName(String name) {
        return "\"" + name + "\"";
    }

    @Override
    public String nextval(String column) {
        return "nextval('" + column + "')";
    }

    @Override
    public String rowSql(String sql, int start, int end) {
        return sql + " limit " + start + "," + end;
    }

    @Override
    public String total(String sql) {
        return null;
    }
}
