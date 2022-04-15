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


    public Condition orderByAsc(String... col) {
        for (String c : col) {
            order += c + ", ";
        }
        order = order.substring(0, order.length() - 2);
        order += " ASC, ";
        return this;
    }

    public Condition orderByDesc(String... col) {
        for (String c : col) {
            order += c + ", ";
        }
        order = order.substring(0, order.length() - 2);
        order += " DESC, ";
        return this;
    }

    public Condition and(String s, Object o) {
        where.add(new SqlCondition(" and ", s, o));
        return this;
    }

    public Condition or(String s, Object o) {
        where.add(new SqlCondition(" and ", s, o));
        return this;
    }

    public String getOrder() {
        if (order.endsWith("by ")){
            return null;
        }
        if (order.endsWith(", ")) {
            order = order.substring(0, order.length() - 2);
        }
        return order;
    }

    public List<SqlCondition> getWhere(){
        return where;
    }
}
