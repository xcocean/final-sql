package top.lingkang.finalsql.sql;

import top.lingkang.finalsql.constants.SqlType;

/**
 * @author lingkang
 * Created by 2022/4/15
 */
public class SqlCondition {
    private String column;
    private Object param;
    private SqlType type;

    public SqlCondition(String column, Object param, SqlType type) {
        this.column = column;
        this.param = param;
        this.type = type;
    }

    public SqlType getType() {
        return type;
    }

    public void setType(SqlType type) {
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
