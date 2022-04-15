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

    public <T> ExSqlEntity querySql(T entity, Condition condition) {
        ExSqlEntity exSqlEntity = columnAndTableAndWhere(entity);
        addCondition(exSqlEntity, condition);
        exSqlEntity.setSql("select " + exSqlEntity.getSql());
        return exSqlEntity;
    }

    public <T> ExSqlEntity oneSql(T entity, Condition condition) {
        ExSqlEntity exSqlEntity = columnAndTableAndWhere(entity);
        addCondition(exSqlEntity, condition);
        String sql = dialect.first().replace("?", exSqlEntity.getSql());
        exSqlEntity.setSql(sql);
        return exSqlEntity;
    }

    public <T> ExSqlEntity countSql(T t, Condition condition) {
        ExSqlEntity exSqlEntity = tableAndWhere(t);
        addCondition(exSqlEntity, condition);
        String sql = dialect.count().replace("?", exSqlEntity.getSql());
        exSqlEntity.setSql(sql);
        return exSqlEntity;
    }

    public <T> ExSqlEntity insertSql(T t) {
        return insert(t);
    }

    public <T> ExSqlEntity updateSql(T t, Condition condition) {
        return update(t, condition);
    }

    public <T> ExSqlEntity deleteSql(T t, Condition condition) {
        return delete(t, condition);
    }

    // --------------------  非主要  ----------------------------------------

    private void addCondition(ExSqlEntity exSqlEntity, Condition condition) {
        if (condition != null) {
            String sql = exSqlEntity.getSql();
            if (sql.indexOf("where") == -1) {
                sql += " where 1=1";
            }
            List<SqlCondition> where = condition.getWhere();
            if (where != null) {
                List<Object> param = exSqlEntity.getParam();
                for (SqlCondition w : where) {
                    sql += " and " + w.getColumn() + "=?, ";
                    param.add(w.getParam());
                }
                sql = sql.substring(0, sql.length() - 2);
                exSqlEntity.setParam(param);
            }
            if (condition.getOrder() != null) {
                sql += condition.getOrder();
            }
            exSqlEntity.setSql(sql);
        }
    }

    private <T> ExSqlEntity tableAndWhere(T entity) {
        Class<?> clazz = ClassUtils.getClass(entity);
        if (entity instanceof Class) {// 如果是类，需要实例化
            try {
                entity = (T) clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new FinalException(e);
            }
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
        Class<?> clazz = ClassUtils.getClass(entity);
        if (entity instanceof Class) {// 如果是类，需要实例化
            try {
                entity = (T) clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new FinalException(e);
            }
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

    private <T> ExSqlEntity update(T entity, Condition condition) {
        Class<?> clazz = entity.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        boolean hasCondition = false;
        if (condition != null && condition.getWhere() != null) {
            hasCondition = true;
        }

        Field idField = null;
        Object id = null;
        if (!hasCondition) {
            idField = ClassUtils.getIdField(declaredFields);
            if (idField == null) {
                throw new FinalSqlException("更新对象中主键Id为空！");
            }
            id = ClassUtils.getValue(entity, clazz, idField.getName());
            if (id == null) {
                throw new FinalSqlException("更新对象中主键Id为空！");
            }
        }

        // 表
        String sql = "update " + NameUtils.getTableName(clazz);
        ExSqlEntity exSqlEntity = new ExSqlEntity();

        sql += " set ";
        List<Object> param = new ArrayList<>();
        for (Field field : declaredFields) {
            Column annotation = field.getAnnotation(Column.class);
            if (annotation != null) {
                Object o = ClassUtils.getValue(entity, clazz, field.getName());
                if (o != null) {// 忽略空值
                    String unHump = StrUtil.isEmpty(annotation.value()) ? NameUtils.unHump(field.getName()) : annotation.value();
                    sql += unHump + "=?, ";
                    param.add(o);
                }
            }
        }

        if (param.size() == 0) {
            throw new FinalSqlException("更新属性不能为空！实体类：" + entity.getClass());
        }

        sql = sql.substring(0, sql.length() - 2);
        if (hasCondition) { // 存在条件
            sql += " where 1=1";
            List<SqlCondition> where = condition.getWhere();
            for (SqlCondition w : where) {
                sql += w.getWhere() + w.getColumn() + "=?, ";
                param.add(w.getParam());
            }
            sql = sql.substring(0, sql.length() - 2);
        } else {
            // 不存在条件按Id更新
            // 主键id
            sql += " where " + idField.getName() + "=?";
            param.add(id);
        }

        exSqlEntity.setParam(param);
        exSqlEntity.setSql(sql);
        return exSqlEntity;
    }

    private <T> ExSqlEntity delete(T entity, Condition condition) {
        Class<?> clazz = ClassUtils.getClass(entity);
        if (entity instanceof Class) {// 如果是类，需要实例化
            try {
                entity = (T) clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new FinalException(e);
            }
        }

        Field[] declaredFields = clazz.getDeclaredFields();
        boolean hasCondition = false;
        if (condition != null && condition.getWhere() != null) {
            hasCondition = true;
        }

        // 表
        String sql = "delete from " + NameUtils.getTableName(clazz);
        ExSqlEntity exSqlEntity = new ExSqlEntity();

        sql += " where ";
        List<Object> param = new ArrayList<>();
        for (Field field : declaredFields) {
            Column annotation = field.getAnnotation(Column.class);
            if (annotation != null) {
                Object o = ClassUtils.getValue(entity, clazz, field.getName());
                if (o != null) { // 忽略空值
                    String unHump = StrUtil.isEmpty(annotation.value()) ? NameUtils.unHump(field.getName()) : annotation.value();
                    sql += unHump + "=? and ";
                    param.add(o);
                }
            }
        }

        if (param.size() == 0 && !hasCondition) {
            throw new FinalSqlException("不支持使用整表数据删除，请添加参数条件！实体类：" + entity.getClass());
        }

        if (hasCondition) { // 存在条件
            List<SqlCondition> where = condition.getWhere();
            for (SqlCondition w : where) {
                sql += w.getColumn() + "=? " + w.getWhere();
                param.add(w.getParam());
            }
        }

        if (sql.endsWith("and ")) {
            sql = sql.substring(0, sql.length() - 4);
        }

        exSqlEntity.setParam(param);
        exSqlEntity.setSql(sql);
        return exSqlEntity;
    }
}
