package top.lingkang.finalsql.dialect;

import top.lingkang.finalsql.sql.ExSqlEntity;

import java.util.ArrayList;

/**
 * @author lingkang
 * Created by 2022/4/18
 */
public class PostgreSqlDialect implements SqlDialect {
    @Override
    public String one(String sql) {
        return "select " + sql + " limit 1 offset 0";
    }

    @Override
    public String count(String sql) {
        return "select count(*) " + sql;
    }

    @Override
    public String getTableName(String name) {
        return "\"" + name + "\"";
    }

    @Override
    public String nextval(String column) {
        return "nextval('" + column + "')";
    }

    @Override
    public String rowSql(String sql, int start, int row) {
        return sql + " limit " + row + " offset " + start;
    }

    @Override
    public ExSqlEntity total(ExSqlEntity sqlEntity) {
        ExSqlEntity entity = new ExSqlEntity(sqlEntity.getSql(), new ArrayList<>(sqlEntity.getParam()));
        String low = entity.getSql().toLowerCase();
        int from = low.indexOf(" from ");
        String temp = entity.getSql().substring(0, from);
        int i1 = temp.indexOf("(");
        if (i1 != -1) {
            do {
                int select = low.indexOf(" select ", i1);
                if (select == -1)
                    select = low.indexOf("(select ", i1);
                if (select != -1 && select < from) {
                    from = low.indexOf(" from ", from + 1);
                    i1 = select;
                }
                i1 = low.indexOf("(", i1 + 1);
            } while (i1 != -1);
        }

        // 检查结果列中的查询, 例如：
        // select id,(select username from user where u.id=id and username=?) from user u order by id desc
        // 入参 ‘lingkang’
        temp = low.substring(0, from);
        if ((i1 = temp.indexOf("?")) != -1) {
            int has = 1;
            for (; ; ) {
                if ((i1 = temp.indexOf("?", i1 + 1)) != -1)
                    has++;
                else
                    break;
            }
            for (; ; ) {
                if (has == 0)
                    break;
                entity.getParam().remove(--has);
            }
        }

        // 检查排序
        i1 = low.indexOf("order");
        if (i1 != -1) {
            entity.setSql("select count(*)" + entity.getSql().substring(from, i1));
        } else {
            entity.setSql("select count(*)" + entity.getSql().substring(from));
        }
        return entity;
    }
}
