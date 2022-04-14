package top.lingkang.finalsql.sql;

import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/13
 */
public class ExSqlEntity {
    private String sql;
    private List<Object> param;

    @Override
    public String toString() {
        return "ExSqlEntity{" +
                "sql='" + sql + '\'' +
                ", param=" + param +
                '}';
    }

    public ExSqlEntity() {
    }

    public ExSqlEntity(String sql) {
        this.sql = sql;
    }

    public ExSqlEntity(String sql, List<Object> param) {
        this.sql = sql;
        this.param = param;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<Object> getParam() {
        return param;
    }

    public void setParam(List<Object> param) {
        this.param = param;
    }
}
