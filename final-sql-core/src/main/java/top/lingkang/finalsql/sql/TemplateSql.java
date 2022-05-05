package top.lingkang.finalsql.sql;

import top.lingkang.finalsql.constants.SqlType;

import java.io.Serializable;

/**
 * @author lingkang
 * Created by 2022/5/5
 */
public class TemplateSql implements Serializable {
    private String sql;
    private SqlType type;

    public TemplateSql() {
    }

    public TemplateSql(String sql, SqlType type) {
        this.sql = sql;
        this.type = type;
    }

    @Override
    public String toString() {
        return "TemplateSql{" +
                "sql='" + sql + '\'' +
                ", type=" + type +
                '}';
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public SqlType getType() {
        return type;
    }

    public void setType(SqlType type) {
        this.type = type;
    }
}
