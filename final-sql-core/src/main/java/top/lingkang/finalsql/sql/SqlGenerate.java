package top.lingkang.finalsql.sql;

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

        // 列
        String sql = NameUtils.getColumn(declaredFields);

        // 表
        sql += " from " + NameUtils.getTableName(clazz);

        ExSqlEntity exSqlEntity = new ExSqlEntity();

        // 条件
        Field[] columnField = ClassUtils.getColumnField(declaredFields);
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

    private <T> ExSqlEntity insert(T entity) {
        String sql = "insert into ";
        Class<?> clazz = entity.getClass();

        // 表
        sql += NameUtils.getTableName(clazz);

        ExSqlEntity exSqlEntity = new ExSqlEntity();

        // 条件
        Field[] columnField = ClassUtils.getColumnField(clazz.getDeclaredFields());
        if (columnField.length < 1) {
            throw new FinalSqlException("插入对象属性不能为空！");
        }

        String val = "";
        sql += " (";
        List<Object> param = new ArrayList<>();
        for (Field field : columnField) {
            Object o = ClassUtils.getValue(entity, clazz, field.getName());
            if (o != null) {
                sql += NameUtils.unHump(field.getName()) + ", ";
                param.add(o);
                val += "?, ";
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

        Field idField = ClassUtils.getIdField(clazz.getDeclaredFields());
        System.out.println(idField);

        // 条件
        Field[] columnField = ClassUtils.getColumnField(clazz.getDeclaredFields());
        if (columnField.length < 1) {
            throw new FinalSqlException("更新对象中主键Id为空！");
        }

        sql += " set ";
        List<Object> param = new ArrayList<>();
        for (Field field : columnField) {
            Object o = ClassUtils.getValue(entity, clazz, field.getName());
            if (ignoreNull && o != null && !field.getName().equals(idField.getName())) {
                sql += NameUtils.unHump(field.getName()) + "=?";
                param.add(o);
            } else if (!ignoreNull && !field.getName().equals(idField.getName())) {
                sql += NameUtils.unHump(field.getName()) + "=?, ";
                param.add(o);
            }
        }
        if (param.size() == 0 && !ignoreNull) {
            throw new FinalSqlException("更新属性不能为空！");
        }
        sql = sql.substring(0, sql.length() - 2);
        sql += " where " + idField.getName() + "=?";
        param.add(ClassUtils.getValue(entity, clazz, idField.getName()));
        exSqlEntity.setParam(param);
        exSqlEntity.setSql(sql);
        return exSqlEntity;
    }
}
