package top.lingkang.finalsql.sql;

import top.lingkang.finalsql.constants.SqlType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/14
 * 条件封装，注意；本框架为轻量级，顾此条件封装为浅层封装<br>
 * 若需要执行复杂条件，请使用
 */
public class Condition {
    private String sql = "";
    private String order = " order by ";
    private List<String> orderByAsc;
    private List<String> orderByDesc;
    private List<SqlCondition> where = new ArrayList<>();
    private boolean custom;
    private List<Object> list = new ArrayList<>();


    /**
     * 正序
     *
     * @param column
     * @return
     */
    public Condition orderByAsc(String... column) {
        for (String c : column) {
            order += c + ", ";
        }
        order = order.substring(0, order.length() - 2);
        order += " ASC, ";
        return this;
    }

    /**
     * 倒序
     *
     * @param column
     * @return
     */
    public Condition orderByDesc(String... column) {
        for (String c : column) {
            order += c + ", ";
        }
        order = order.substring(0, order.length() - 2);
        order += " DESC, ";
        return this;
    }

    /**
     * 等于条件：and column=value
     *
     * @param column
     * @param value
     * @return
     */
    public Condition eq(String column, Object value) {
        where.add(new SqlCondition(column, value, SqlType.AND));
        return this;
    }

    /**
     * 或条件：or column=value
     *
     * @param column
     * @param value
     * @return
     */
    public Condition or(String column, Object value) {
        where.add(new SqlCondition(column, value, SqlType.OR));
        return this;
    }

    /**
     * 与 in 条件：and column in (value1, value2, value3)
     *
     * @param column
     * @param value
     * @return
     */
    public Condition andIn(String column, List value) {
        where.add(new SqlCondition(column, value, SqlType.AND_IN));
        return this;
    }

    /**
     * 或 in 条件：or column in (value1, value2, value3)
     *
     * @param column
     * @param value
     * @return
     */
    public Condition orIn(String column, List value) {
        where.add(new SqlCondition(column, value, SqlType.OR_IN));
        return this;
    }

    /**
     * 模糊搜索条件：and column like %value%
     *
     * @param column
     * @param value
     * @return
     */
    public Condition like(String column, Object value) {
        where.add(new SqlCondition(column, value, SqlType.LIKE));
        return this;
    }

    /**
     * 左搜索条件：and column like %value
     *
     * @param column
     * @param value
     * @return
     */
    public Condition leftLike(String column, Object value) {
        where.add(new SqlCondition(column, value, SqlType.LEFT_LIKE));
        return this;
    }

    /**
     * 右搜索条件：and column like value%
     *
     * @param column
     * @param value
     * @return
     */
    public Condition rightLike(String column, Object value) {
        where.add(new SqlCondition(column, value, SqlType.RIGHT_LIKE));
        return this;
    }

    /**
     * 不等于条件：and column <> value
     *
     * @param column
     * @param value
     * @return
     */
    public Condition ne(String column, Object value) {
        where.add(new SqlCondition(column, value, SqlType.NE));
        return this;
    }

    /**
     * 大于条件：and column > value
     *
     * @param column
     * @param value
     * @return
     */
    public Condition gt(String column, Object value) {
        where.add(new SqlCondition(column, value, SqlType.GT));
        return this;
    }

    /**
     * 大于等于条件：and column >= value
     *
     * @param column
     * @param value
     * @return
     */
    public Condition ge(String column, Object value) {
        where.add(new SqlCondition(column, value, SqlType.GE));
        return this;
    }

    /**
     * 小于条件：and column < value
     *
     * @param column
     * @param value
     * @return
     */
    public Condition lt(String column, Object value) {
        where.add(new SqlCondition(column, value, SqlType.LT));
        return this;
    }

    /**
     * 小于等于条件：and column <= value
     *
     * @param column
     * @param value
     * @return
     */
    public Condition le(String column, Object value) {
        where.add(new SqlCondition(column, value, SqlType.LE));
        return this;
    }

    /**
     * 自定义条件（支持复杂），只能定义一个，多个时将被后面的覆盖.<br>
     * 用法：new Condition().custom("and create_time < ? and id<3",new Date())));
     * sql 中的 ? 将被参数替换，需要注意个数与参数一致
     * 注意，使用list、入参时，需要自己添加多个?，例如 custom("and id in (?,?,?,?)",1,2,3,4)
     * 您可以使用 Condition.getIn(size) 例如 Condition.getIn(4) => ?,?,?,? 获取 参数个数
     *
     * @param sql   自定义sql条件 例如： and a=? and t>?
     * @param param 条件参数 例如：   "123","2022-04-16 00:00:00"
     * @return
     */
    public Condition custom(String sql, Object... param) {
        custom = true;
        for (Object o : param) {
            list.add(o);
        }
        this.sql = " " + sql;
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

    public ExSqlEntity getSql() {
        if (custom) { // 使用自定义条件
            return new ExSqlEntity(sql, list);
        }
        List<Object> param = new ArrayList<>();
        for (SqlCondition condition : where) {
            switch (condition.getType()) {
                case AND:
                    sql += " and " + condition.getColumn() + "=?";
                    param.add(condition.getParam());
                    break;
                case OR:
                    sql += " or " + condition.getColumn() + "=?";
                    param.add(condition.getParam());
                    break;
                case AND_IN:
                    List list = (List) condition.getParam();
                    if (list.isEmpty()) {
                        break;
                    }
                    setIn(sql, param, list, condition, true);
                    break;
                case OR_IN:
                    list = (List) condition.getParam();
                    if (list.isEmpty()) {
                        break;
                    }
                    setIn(sql, param, list, condition, false);
                    break;
                case LIKE:
                    sql += " and " + condition.getColumn() + " like ?";
                    param.add("%"+condition.getParam()+"%");
                    break;
                case LEFT_LIKE:
                    sql += " and " + condition.getColumn() + " like ?";
                    param.add("%"+condition.getParam());
                    break;
                case RIGHT_LIKE:
                    sql += " and " + condition.getColumn() + " like ?";
                    param.add(condition.getParam()+"%");
                    break;
                case NE:
                    sql += " and " + condition.getColumn() + " <> ?";
                    param.add(condition.getParam());
                    break;
                case GT:
                    sql += " and " + condition.getColumn() + " > ?";
                    param.add(condition.getParam());
                    break;
                case GE:
                    sql += " and " + condition.getColumn() + " >= ?";
                    param.add(condition.getParam());
                    break;
                case LT:
                    sql += " and " + condition.getColumn() + " < ?";
                    param.add(condition.getParam());
                    break;
                case LE:
                    sql += " and " + condition.getColumn() + " <= ?";
                    param.add(condition.getParam());
                    break;
            }
        }
        return new ExSqlEntity(sql, param);
    }

    public boolean hasWhere() {
        return !where.isEmpty() || custom;
    }

    //  ---------------------- 其他 ------------------------------------------------------------------
    private void setIn(String sql, List param, List list, SqlCondition condition, boolean isAnd) {
        String in = "";
        for (int i = 0; i < list.size(); i++) {
            in += "?,";
            param.add(list.get(i));
        }
        sql += (isAnd ? " and " : " or ") + condition.getColumn() + " in (" + in.substring(0, in.length() - 1) + ")";
        this.sql = sql;
    }

    public static String getIn(int size) {
        String in = "";
        for (int i = 0; i < size; i++) {
            in += "?,";
        }
        return in.substring(0, in.length() - 1);
    }
}
