package top.lingkang.finalsql.sql;

/**
 * @author lingkang
 * Created by 2022/4/15
 */
public class SqlCondition {
    private String where;
    private String column;
    private Object param;

    public SqlCondition(String where, String column, Object param) {
        this.where = where;
        this.column = column;
        this.param = param;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }
}
