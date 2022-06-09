package top.lingkang.finalsql.sql;

import top.lingkang.finalsql.annotation.Column;
import top.lingkang.finalsql.annotation.Id;
import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.constants.IdType;
import top.lingkang.finalsql.dialect.SqlDialect;
import top.lingkang.finalsql.error.FinalException;
import top.lingkang.finalsql.error.FinalSqlException;
import top.lingkang.finalsql.utils.ClassUtils;
import top.lingkang.finalsql.utils.CommonUtils;
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
    private SqlConfig sqlConfig;

    public SqlGenerate(SqlDialect dialect, SqlConfig sqlConfig) {
        this.dialect = dialect;
        this.sqlConfig = sqlConfig;
    }

    public <T> ExSqlEntity querySql(T entity, Condition condition) {
        ExSqlEntity exSqlEntity = columnAndTableAndWhere(entity);
        addQueryCondition(exSqlEntity, condition);
        exSqlEntity.setSql("select " + exSqlEntity.getSql());
        return exSqlEntity;
    }

    public <T> ExSqlEntity oneSql(T entity, Condition condition) {
        ExSqlEntity exSqlEntity = columnAndTableAndWhere(entity);
        addQueryCondition(exSqlEntity, condition);
        exSqlEntity.setSql(dialect.one(exSqlEntity.getSql()));
        return exSqlEntity;
    }

    public <T> ExSqlEntity countSql(T t, Condition condition) {
        ExSqlEntity exSqlEntity = tableAndWhere(t);
        addCondition(exSqlEntity, condition);
        exSqlEntity.setSql(dialect.count(exSqlEntity.getSql()));
        return exSqlEntity;
    }

    public <T> ExSqlEntity selectRowSql(ExSqlEntity exSqlEntity, int row) {
        exSqlEntity.setSql(dialect.rowSql(exSqlEntity.getSql(), 0, row));
        return exSqlEntity;
    }

    public <T> ExSqlEntity insertSql(T t) {
        // 检查id
        this.checkId(t);
        return insert(t);
    }

    public <T> ExSqlEntity batchInsert(List<T> entity) {
        String sql = "";
        List<Object> param = new ArrayList<>();
        boolean isFirst = false;
        int start = 0, eq = entity.size() - 1;
        for (int i = 0; i < entity.size(); i++) {
            T t = entity.get(i);
            ExSqlEntity exSqlEntity = this.insertSql(t);
            if (!isFirst) {
                sql += exSqlEntity.getSql() + "";
                start = sql.indexOf("values") + 7;
                isFirst = true;
            } else {
                sql += exSqlEntity.getSql().substring(start);
            }
            if (i == eq) {
                sql += ";";
            } else {
                sql += ",\n";
            }
            param.addAll(exSqlEntity.getParam());
        }
        return new ExSqlEntity(sql, param);
    }

    public <T> ExSqlEntity updateSql(T t, Condition condition) {
        return update(t, condition);
    }

    public <T> ExSqlEntity deleteSql(T t, Condition condition) {
        return delete(t, condition);
    }

    public <T> ExSqlEntity deleteSql(Class<T> t, List<Object> ids) {
        return deleteByIds(t, ids);
    }

    // --------------------  非主要  ----------------------------------------

    private void addQueryCondition(ExSqlEntity exSqlEntity, Condition condition) {
        if (condition != null) {
            String sql = exSqlEntity.getSql();
            if (sql.indexOf("where") == -1) {
                sql += " where 1=1";
            }
            ExSqlEntity sql1 = condition.getSql();
            sql += sql1.getSql();
            exSqlEntity.getParam().addAll(sql1.getParam());
            if (condition.getOrder() != null) {
                sql += condition.getOrder();
            }
            exSqlEntity.setSql(sql);
        }
    }

    private void addCondition(ExSqlEntity exSqlEntity, Condition condition) {
        if (condition != null) {
            String sql = exSqlEntity.getSql();
            if (sql.indexOf("where") == -1) {
                sql += " where 1=1";
            }
            ExSqlEntity sql1 = condition.getSql();
            sql += sql1.getSql();
            exSqlEntity.getParam().addAll(sql1.getParam());
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
        String sql = " from " + NameUtils.getTableName(clazz, dialect);

        ExSqlEntity exSqlEntity = new ExSqlEntity();

        // 条件
        Field[] declaredFields = clazz.getDeclaredFields();
        if (declaredFields.length > 0) {
            sql += " where 1=1";
            List<Object> param = new ArrayList<>();
            for (Field field : clazz.getDeclaredFields()) {
                Object o = ClassUtils.getValue(entity, clazz, field.getName());
                Column annotation = field.getAnnotation(Column.class);
                if (o != null && annotation != null) {
                    param.add(o);
                    sql += " and ";
                    if (!"".equals(annotation.value())) {
                        sql += field.getName() + "=?";
                    } else {
                        sql += NameUtils.unHump(field.getName()) + "=?";
                    }
                }
            }
            exSqlEntity.setParam(param);
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

        String col = "", sql = " where 1=1";
        List<Object> param = new ArrayList<>();
        if (declaredFields.length > 0) {
            for (Field field : declaredFields) {
                Column annotation = field.getAnnotation(Column.class);
                if (annotation != null) {
                    // 列和条件
                    String unHump = CommonUtils.isEmpty(annotation.value()) ? NameUtils.unHump(field.getName()) : annotation.value();
                    if (unHump.equals(field.getName()))
                        col += unHump + ", ";
                    else
                        col += unHump + " as " + field.getName() + ", ";

                    // 参数
                    Object value = ClassUtils.getValue(entity, clazz, field.getName());
                    if (value != null) {
                        param.add(value);
                        sql += " and " + unHump + "=?";
                    }
                }
            }
            if (CommonUtils.isEmpty(col)) {
                col = " * ";
            } else {
                col = col.substring(0, col.length() - 2);
            }
        }

        // 列+表+条件
        sql = col + " from " + NameUtils.getTableName(clazz, dialect) + sql;

        ExSqlEntity exSqlEntity = new ExSqlEntity();
        exSqlEntity.setSql(sql);
        exSqlEntity.setParam(param);
        return exSqlEntity;
    }

    private <T> ExSqlEntity insert(T entity) {
        Class<?> clazz = entity.getClass();

        // 表
        String sql = "insert into " + NameUtils.getTableName(clazz, dialect);

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
            Column column = field.getAnnotation(Column.class);
            Id id = field.getAnnotation(Id.class);
            if (id != null && id.value() == IdType.AUTO) {
                if ("".equals(id.sequence())) {
                    continue;// 自动生成跳过
                }
                String nextval = dialect.nextval(id.sequence());
                // Object o = ClassUtils.getValue(entity, clazz, field.getName());
                String unHump = CommonUtils.isEmpty(column.value()) ? NameUtils.unHump(field.getName()) : column.value();
                sql += unHump + ", ";
                // postgresql类协议
                val += nextval + ", ";
            } else if (column != null) {
                Object o = ClassUtils.getValue(entity, clazz, field.getName());
                if (o != null) {
                    String unHump = CommonUtils.isEmpty(column.value()) ? NameUtils.unHump(field.getName()) : column.value();
                    sql += unHump + ", ";
                    param.add(o);
                    val += "?, ";
                }
            }
        }

        if (param.isEmpty()) {
            throw new FinalSqlException("不能插入空对象：" + entity);
        }

        exSqlEntity.setParam(param);
        sql = sql.substring(0, sql.length() - 2) + ")";
        sql += " values (" + val.substring(0, val.length() - 2) + ")";
        exSqlEntity.setSql(sql);
        return exSqlEntity;
    }

    private <T> ExSqlEntity update(T entity, Condition condition) {
        Class<?> clazz = entity.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        boolean hasCondition = false;
        if (condition != null && condition.hasWhere()) {
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
        String sql = "update " + NameUtils.getTableName(clazz, dialect);
        ExSqlEntity exSqlEntity = new ExSqlEntity();

        sql += " set ";
        List<Object> param = new ArrayList<>();
        for (Field field : declaredFields) {
            Column annotation = field.getAnnotation(Column.class);
            if (annotation != null) {
                Object o = ClassUtils.getValue(entity, clazz, field.getName());
                if (o != null) {// 忽略空值
                    String unHump = CommonUtils.isEmpty(annotation.value()) ? NameUtils.unHump(field.getName()) : annotation.value();
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
            ExSqlEntity exSql = condition.getSql();
            sql += exSql.getSql();
            exSqlEntity.getParam().addAll(exSql.getParam());
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
        if (entity instanceof Class) {
            try {// 实例化
                entity = (T) ((Class) entity).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Class<?> clazz = entity.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        boolean hasCondition = false;
        if (condition != null && condition.hasWhere()) {
            hasCondition = true;
        }

        // 表
        String sql = "delete from " + NameUtils.getTableName(clazz, dialect);
        ExSqlEntity exSqlEntity = new ExSqlEntity();

        sql += " where 1=1";
        List<Object> param = new ArrayList<>();
        for (Field field : declaredFields) {
            Column annotation = field.getAnnotation(Column.class);
            Object o = ClassUtils.getValue(entity, clazz, field.getName());
            if (o != null) { // 忽略空值
                String unHump = CommonUtils.isEmpty(annotation.value()) ? NameUtils.unHump(field.getName()) : annotation.value();
                sql += " and " + unHump + "=?";
                param.add(o);
                break;
            }
        }

        if (param.size() == 0 && !hasCondition) {
            throw new FinalSqlException("不支持使用整表数据删除，请添加参数条件！实体类：" + entity.getClass() + "\n 若想整表数据删除，可添加条件 1=1");
        }

        if (hasCondition) { // 存在条件
            ExSqlEntity exSql = condition.getSql();
            sql += exSql.getSql();
            param.addAll(exSql.getParam());
        }

        exSqlEntity.setParam(param);
        exSqlEntity.setSql(sql);
        return exSqlEntity;
    }

    private <T> ExSqlEntity deleteByIds(Class<T> t, List<Object> ids) {
        // 表
        String sql = "delete from " + NameUtils.getTableName(t, dialect);
        Field idColumn = ClassUtils.getIdColumn(t.getDeclaredFields());
        if (idColumn == null) {
            throw new FinalException("对象中找不到 @Id 注解，无法获取 Id 字段");
        }
        sql += " where " + NameUtils.unHump(idColumn.getName()) + " in (" + Condition.getIn(ids.size()) + ")";
        return new ExSqlEntity(sql, ids);
    }

    private <T> void checkId(T entity) {
        // 检查id
        Id annotation = null;
        Field id = null;
        for (Field field : entity.getClass().getDeclaredFields()) {
            annotation = field.getAnnotation(Id.class);
            if (annotation != null) {
                id = field;
                break;
            }
        }
        if (annotation == null)
            return;
        if (annotation.value() == IdType.INPUT) {
            if (ClassUtils.getValue(entity, entity.getClass(), id.getName()) == null) {
                throw new FinalException("实体对象 @Id 类型为 IdType.INPUT，则主键 id 的值不能为空！");
            }
        }
    }
}
