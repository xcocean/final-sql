package top.lingkang.finalsql.sql;

import cn.hutool.core.util.StrUtil;
import top.lingkang.finalsql.annotation.Column;
import top.lingkang.finalsql.dialect.SqlDialect;
import top.lingkang.finalsql.error.FinalException;
import top.lingkang.finalsql.error.FinalSqlException;
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
        String sql = dialect.first().replace("?", exSqlEntity.getSql());
        exSqlEntity.setSql(sql);
        return exSqlEntity;
    }

    public <T> ExSqlEntity countSql(T t) {
        ExSqlEntity exSqlEntity = tableAndWhere(t);
        String sql = dialect.count().replace("?", exSqlEntity.getSql());
        exSqlEntity.setSql(sql);
        return exSqlEntity;
    }

    public <T> ExSqlEntity insertSql(T t) {
        return insert(t);
    }

    public <T> ExSqlEntity updateSql(T t) {
        return update(t, true);
    }

    // --------------------  非主要  ----------------------------------------

    private <T> ExSqlEntity tableAndWhere(T entity) {
        Class<?> clazz = null;
        if (entity instanceof Class) {
            clazz = ClassUtils.getClass(entity);
            try {
                entity = (T) clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new FinalException(e);
            }
        } else {
            clazz = entity.getClass();
        }

        // 表
        String sql = " from " + NameUtils.getTableName(clazz);

        ExSqlEntity exSqlEntity = new ExSqlEntity();

        // 条件
        Field[] declaredFields = clazz.getDeclaredFields();
        if (declaredFields.length > 0) {
            sql += " where ";
            List<Object> param = new ArrayList<>();
            for (Field field : clazz.getDeclaredFields()) {
                Object o = ClassUtils.getValue(entity, clazz, field.getName());
                Column annotation = field.getAnnotation(Column.class);
                if (o != null && annotation != null) {
                    param.add(o);
                    if (!"".equals(annotation.value())) {
                        sql += field.getName() + "=? and ";
                    } else {
                        sql += NameUtils.unHump(field.getName()) + "=? and ";
                    }
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
        Class<?> clazz = null;
        if (entity instanceof Class) {
            clazz = ClassUtils.getClass(entity);
            try {
                entity = (T) clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new FinalException(e);
            }
        } else {
            clazz = entity.getClass();
        }

        Field[] declaredFields = clazz.getDeclaredFields();

        String col = "", sql = " where ";
        List<Object> param = new ArrayList<>();
        if (declaredFields.length > 0) {
            for (Field field : declaredFields) {
                Column annotation = field.getAnnotation(Column.class);
                if (annotation != null) {
                    // 列和条件
                    String unHump = StrUtil.isEmpty(annotation.value()) ? NameUtils.unHump(field.getName()) : annotation.value();
                    if (unHump.equals(field.getName()))
                        col += unHump + ", ";
                    else
                        col += unHump + " as " + field.getName() + ", ";

                    // 参数
                    Object value = ClassUtils.getValue(entity, clazz, field.getName());
                    if (value != null) {
                        param.add(value);
                        sql += unHump + "=? and ";
                    }
                }
            }
            if (StrUtil.isEmpty(col)) {
                col = " * ";
            } else {
                col = col.substring(0, col.length() - 2);
            }
            if (sql.endsWith("where "))
                sql = sql.substring(0, sql.length() - 7);
            else if (sql.endsWith("and "))
                sql = sql.substring(0, sql.length() - 5);
        }

        // 列+表+条件
        sql = col + " from " + NameUtils.getTableName(clazz) + sql;

        ExSqlEntity exSqlEntity = new ExSqlEntity();
        exSqlEntity.setSql(sql);
        exSqlEntity.setParam(param);
        return exSqlEntity;
    }

    private <T> ExSqlEntity insert(T entity) {
        Class<?> clazz = entity.getClass();

        // 表
        String sql = "insert into " + NameUtils.getTableName(clazz);

        ExSqlEntity exSqlEntity = new ExSqlEntity();

        // 条件
        Field[] declaredFields = clazz.getDeclaredFields();
        if (declaredFields.length < 1) {
            throw new FinalSqlException("插入对象属性不能为空！");
        }

        String val = "";
        sql += " (";
        List<Object> param = new ArrayList<>();
        for (Field field : declaredFields) {
            Column annotation = field.getAnnotation(Column.class);
            if (annotation != null) {
                Object o = ClassUtils.getValue(entity, clazz, field.getName());
                if (o != null) {
                    String unHump = StrUtil.isEmpty(annotation.value()) ? NameUtils.unHump(field.getName()) : annotation.value();
                    sql += unHump + ", ";
                    param.add(o);
                    val += "?, ";
                }
            }
        }

        exSqlEntity.setParam(param);
        sql = sql.substring(0, sql.length() - 2) + ")";
        sql += " values (" + val.substring(0, val.length() - 2) + ");";
        exSqlEntity.setSql(sql);
        return exSqlEntity;
    }

    private <T> ExSqlEntity update(T entity, boolean ignoreNull) {
        String sql = "update ";
        Class<?> clazz = entity.getClass();

        // 表
        sql += NameUtils.getTableName(clazz);

        ExSqlEntity exSqlEntity = new ExSqlEntity();

        Field[] declaredFields = clazz.getDeclaredFields();

        // 活动id
        Field idField = ClassUtils.getIdField(declaredFields);
        Object id = ClassUtils.getValue(entity, clazz, idField.getName());
        if (idField == null || id == null) {
            throw new FinalSqlException("更新对象中主键Id为空！");
        }

        sql += " set ";
        List<Object> param = new ArrayList<>();
        for (Field field : declaredFields) {
            Column annotation = field.getAnnotation(Column.class);
            if (annotation != null) {
                if (idField.getName().equals(field.getName()))// 不需要添加主键更新
                    continue;

                Object o = ClassUtils.getValue(entity, clazz, field.getName());
                if (ignoreNull) {
                    if (o != null) {
                        String unHump = StrUtil.isEmpty(annotation.value()) ? NameUtils.unHump(field.getName()) : annotation.value();
                        sql += unHump + "=?, ";
                        param.add(o);
                    }
                } else {
                    String unHump = StrUtil.isEmpty(annotation.value()) ? NameUtils.unHump(field.getName()) : annotation.value();
                    sql += unHump + "=?, ";
                    param.add(o);
                }
            }
        }

        if (param.size() == 0 && !ignoreNull) {
            throw new FinalSqlException("更新属性不能为空！");
        }


        sql = sql.substring(0, sql.length() - 2);
        sql += " where " + idField.getName() + "=?";
        param.add(id);
        exSqlEntity.setParam(param);
        exSqlEntity.setSql(sql);
        return exSqlEntity;
    }
}
