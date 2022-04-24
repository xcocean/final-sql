package top.lingkang.finalsql.dialect;

/**
 * @author lingkang
 * Created by 2022/4/12
 */
public class Mysql57Dialect implements SqlDialect {
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
        return name;
    }

    @Override
    public String nextval(String column) {
        return null;
    }

    @Override
    public String rowSql(String sql, int row) {
        return sql + " limit " + row;
    }
}
