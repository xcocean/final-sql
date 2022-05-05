package top.lingkang.finalsql.sql;

import top.lingkang.finalsql.constants.WhereType;

/**
 * @author lingkang
 * Created by 2022/4/15
 */
public class SqlCondition {
    private String column;
    private Object param;
    private WhereType type;

    public SqlCondition(String column, Object param, WhereType type) {
        this.column = column;
        this.param = param;
        this.type = type;
    }

    public WhereType getType() {
        return type;
    }

    public void setType(WhereType type) {
        this.type = type;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }


}
