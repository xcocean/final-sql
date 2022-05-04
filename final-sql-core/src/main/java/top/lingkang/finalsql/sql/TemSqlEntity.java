package top.lingkang.finalsql.sql;

import top.lingkang.finalsql.constants.ExType;

import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/13
 */
public class TemSqlEntity {
    private String sql;
    private ExType type;

    public TemSqlEntity() {
    }

    public TemSqlEntity(String sql, ExType type) {
        this.sql = sql;
        this.type = type;
    }

    @Override
    public String toString() {
        return "TemSqlEntity{" +
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

    public ExType getType() {
        return type;
    }

    public void setType(ExType type) {
        this.type = type;
    }
}
