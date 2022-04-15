package top.lingkang.finalsql.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/14
 */
public class Condition {
    private String sql = "";
    private String order = " order by ";
    private List<String> orderByAsc;
    private List<String> orderByDesc;
    private List<SqlCondition> where = new ArrayList<>();


    public Condition orderByAsc(String... column) {
        for (String c : column) {
            order += c + ", ";
        }
        order = order.substring(0, order.length() - 2);
        order += " ASC, ";
        return this;
    }

    public Condition orderByDesc(String... column) {
        for (String c : column) {
            order += c + ", ";
        }
        order = order.substring(0, order.length() - 2);
        order += " DESC, ";
        return this;
    }

    public Condition and(String column, Object value) {
        where.add(new SqlCondition(" and ", column, value));
        return this;
    }

    public Condition or(String column, Object value) {
        where.add(new SqlCondition(" and ", column, value));
        return this;
    }

    public String getOrder() {
        if (order.endsWith("by ")) {
            return null;
        }
        if (order.endsWith(", ")) {
            order = order.substring(0, order.length() - 2);
        }
        return order;
    }

    public List<SqlCondition> getWhere() {
        return where;
    }
}
