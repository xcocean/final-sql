package top.lingkang.finalsql.sql;

import cn.hutool.core.util.StrUtil;
import top.lingkang.finalsql.annotation.Table;
import top.lingkang.finalsql.dialect.SqlDialect;
import top.lingkang.finalsql.utils.ClassUtils;
import top.lingkang.finalsql.utils.NameUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
public class SqlGenerate {
    private SqlDialect dialect;

    public SqlGenerate(SqlDialect dialect) {
        this.dialect = dialect;
    }

    public <T> ExSqlEntity querySql(T entity) {
        ExSqlEntity exSqlEntity = columnAndTableAndWhere(entity);
        exSqlEntity.setSql("select " + exSqlEntity.getSql());
        return exSqlEntity;
    }

    public <T> ExSqlEntity oneSql(T t) {
        ExSqlEntity exSqlEntity = columnAndTableAndWhere(t);
        if (exSqlEntity.getParam().size() > 0) {
            String sql = dialect.first().replace("?", exSqlEntity.getSql());
            exSqlEntity.setSql(sql);
        }
        return exSqlEntity;
    }

    public <T> ExSqlEntity countSql(T t) {
        ExSqlEntity exSqlEntity = tableAndWhere(t);
        String sql = dialect.count().replace("?", exSqlEntity.getSql());
        exSqlEntity.setSql(sql);
        return exSqlEntity;
    }

    // --------------------  非主要  ----------------------------------------

    private <T> ExSqlEntity tableAndWhere(T entity) {
        String sql = "";
        Class<?> clazz = entity.getClass();

        // 表
        Table annotation = clazz.getAnnotation(Table.class);
        if (annotation != null && StrUtil.isNotEmpty(annotation.value())) {
            sql += " from " + annotation.value();
        } else {
            sql += " from " + NameUtils.unHump(clazz.getSimpleName());
        }

        ExSqlEntity exSqlEntity = new ExSqlEntity();

        // 条件
        Field[] columnField = ClassUtils.getColumnField(clazz.getDeclaredFields());
        if (columnField.length > 0) {
            sql += " where ";
            List<Object> param = new ArrayList<>();
            for (Field field : columnField) {
                Object o = ClassUtils.getValue(entity, clazz, field.getName());
                if (o != null) {
                    sql += field.getName() + "=? and ";
                    param.add(o);
                }
            }
            exSqlEntity.setParam(param);
            if (sql.endsWith("where "))
                sql = sql.substring(0, sql.length() - 7);
            else if (sql.endsWith("and "))
                sql = sql.substring(0, sql.length() - 5);
        }

        exSqlEntity.setSql(sql);
        return exSqlEntity;
    }

    private <T> ExSqlEntity columnAndTableAndWhere(T entity) {
        String sql = "";
        Class<?> clazz = entity.getClass();
        // 列
        String column = ClassUtils.getColumn(clazz.getDeclaredFields());
        if (column != null) {
            sql += column;
        } else {
            sql += "*";
        }

        // 表
        Table annotation = clazz.getAnnotation(Table.class);
        if (annotation != null && StrUtil.isNotEmpty(annotation.value())) {
            sql += " from " + annotation.value();
        } else {
            sql += " from " + NameUtils.unHump(clazz.getSimpleName());
        }

        ExSqlEntity exSqlEntity = new ExSqlEntity();

        // 条件
        Field[] columnField = ClassUtils.getColumnField(clazz.getDeclaredFields());
        if (columnField.length > 0) {
            sql += " where ";
            List<Object> param = new ArrayList<>();
            for (Field field : columnField) {
                Object o = ClassUtils.getValue(entity, clazz, field.getName());
                if (o != null) {
                    sql += field.getName() + "=? and ";
                    param.add(o);
                }
            }
            exSqlEntity.setParam(param);
            if (sql.endsWith("where "))
                sql = sql.substring(0, sql.length() - 7);
            else if (sql.endsWith("and "))
                sql = sql.substring(0, sql.length() - 5);
        }

        exSqlEntity.setSql(sql);
        return exSqlEntity;
    }
}
