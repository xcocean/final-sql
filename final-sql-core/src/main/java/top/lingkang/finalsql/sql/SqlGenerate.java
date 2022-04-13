package top.lingkang.finalsql.sql;

import cn.hutool.core.util.StrUtil;
import top.lingkang.finalsql.SqlConfig;
import top.lingkang.finalsql.annotation.Table;
import top.lingkang.finalsql.utils.AnnotationUtils;
import top.lingkang.finalsql.utils.NameUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
public class SqlGenerate {
    private SqlConfig sqlConfig;

    public SqlGenerate(SqlConfig sqlConfig) {
        this.sqlConfig = sqlConfig;
    }

    public <T> ExSqlEntity querySql(T t) {
        String sql = "select ";
        Class<?> clazz = t.getClass();
        // 行
        String column = AnnotationUtils.getColumn(clazz.getDeclaredFields());
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
        Field[] columnField = AnnotationUtils.getColumnField(clazz.getDeclaredFields());
        if (columnField.length > 0) {
            sql += " where ";
            List<Object> param = new ArrayList<>();
            for (Field field : columnField) {
                Object o = getValue(t, clazz, field.getName());
                if (o != null) {
                    sql += field.getName() + "=? and ";
                    param.add(o);
                }
            }
            exSqlEntity.setParam(param);
            sql = sql.substring(0, sql.length() - 4);
        }

        exSqlEntity.setSql(sql);
        return exSqlEntity;
    }

    public <T> ExSqlEntity oneSql(T t) {
        ExSqlEntity exSqlEntity = querySql(t);
        if (exSqlEntity.getParam().size() > 0) {
            String sql = sqlConfig.getSqlDialect().queryOne().replace("?", exSqlEntity.getSql().substring(7));
            exSqlEntity.setSql(sql);
        }
        return exSqlEntity;
    }

    public <T> ExSqlEntity countSql(T t) {
        ExSqlEntity exSqlEntity = querySql(t);
        exSqlEntity.setSql("select count(*) " + exSqlEntity.getSql().substring(7));
        return exSqlEntity;
    }


    private static <T> Object getValue(T t, Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(t);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
