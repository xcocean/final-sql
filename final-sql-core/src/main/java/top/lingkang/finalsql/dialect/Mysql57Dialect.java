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
    public String rowSql(String sql, int start, int end) {
        return sql + " limit " + start + "," + end;
    }

    @Override
    public String total(String sql) {
        String low = sql.toLowerCase();
        int from = low.indexOf(" from ");
        String temp = sql.substring(0, from);
        int i1 = temp.indexOf("(");
        if (i1 != -1) {
            do {
                from = low.indexOf(" from ", from + 1);
                i1 = low.indexOf("(", i1 + 1);
            } while (i1 != -1);
        }
        return "select count(*) " + sql.substring(from);
    }
}
