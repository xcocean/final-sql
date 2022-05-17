package top.lingkang.finalsql.sql;


import cn.hutool.core.lang.Assert;
import top.lingkang.finalsql.annotation.Nullable;
import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.error.ResultHandlerException;
import top.lingkang.finalsql.utils.ClassUtils;
import top.lingkang.finalsql.utils.NameUtils;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
public class ResultHandler {
    private SqlConfig sqlConfig;

    public ResultHandler(SqlConfig config) {
        this.sqlConfig = config;
    }

    @Nullable
    public <T> List<T> list(ResultSet resultSet, T entity) {
        Assert.notNull(resultSet, "ResultSet 结果集不能为空！");
        try {
            List<T> list = new ArrayList<>();
            //获取要封装的javabean声明的属性
            Class<?> clazz = ClassUtils.getClass(entity);
            Field[] fields = ClassUtils.getColumnField(clazz.getDeclaredFields());
            //遍历ResultSet
            while (resultSet.next()) {
                T obj = (T) clazz.newInstance();
                //匹配JavaBean的属性,然后赋值
                for (Field field : fields) {
                    field.setAccessible(true);
                    field.set(obj, resultSet.getObject(field.getName(), field.getType()));
                }
                list.add(obj);
            }
            return list;
        } catch (SQLException | IllegalAccessException | InstantiationException e) {
            throw new ResultHandlerException(e);
        }
    }

    public <T> List<T> selectForList(ResultSet result, Class<T> entity) throws Exception {
        List<T> list = new ArrayList<>();
        for (; ; ) {
            T t = selectForObject(result, entity);
            if (t == null)
                break;
            list.add(t);
        }
        return list;
    }

    public <T> T selectForObject(ResultSet result, Class<T> entity) throws Exception {
        if (entity == Map.class) {
            if (result.next())
                return (T) selectForMap(result);
        } else if (ClassUtils.isBaseWrapper(entity)) {
            if (result.next())
                return result.getObject(1, entity);
        } else if (entity == Blob.class) {
            if (result.next())
                return (T) result.getBlob(1);
        } else {
            if (result.next()) {
                ResultSetMetaData metaData = result.getMetaData();
                T ins = entity.newInstance();// 实例化对象
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    Field field = ClassUtils.getField(
                            NameUtils.toHump(metaData.getColumnLabel(i)),
                            ins.getClass().getDeclaredFields()
                    );
                    if (field != null) {
                        field.setAccessible(true);
                        field.set(ins, result.getObject(i, field.getType()));
                    }
                }
                return ins;
            }
        }
        return null;
    }


    public <T> T one(ResultSet resultSet, T entity) {
        Assert.notNull(resultSet, "ResultSet 结果集不能为空！");
        try {
            //获取要封装的javabean声明的属性
            Class<?> clazz = entity.getClass();
            Field[] fields = ClassUtils.getColumnField(clazz.getDeclaredFields());

            //匹配JavaBean的属性,然后赋值
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(entity, resultSet.getObject(field.getName(), field.getType()));
            }
            return entity;
        } catch (SQLException | IllegalAccessException e) {
            throw new ResultHandlerException(e);
        }
    }

    public <T> int insert(ResultSet resultSet, T entity) throws SQLException, IllegalAccessException {
        int row = resultSet.getRow();
        Class<?> clazz = entity.getClass();
        Field idColumn = ClassUtils.getIdColumn(clazz.getDeclaredFields());
        if (idColumn != null) {
            idColumn.setAccessible(true);
            idColumn.set(entity, resultSet.getObject(1, idColumn.getType()));
        }
        return row;
    }

    public <T> int batchInsert(ResultSet resultSet, List<T> entity) throws SQLException, IllegalAccessException {
        int row = resultSet.getRow();
        int i = 0;
        while (resultSet.next()) {
            Class<?> clazz = entity.get(i).getClass();
            Field idColumn = ClassUtils.getIdColumn(clazz.getDeclaredFields());
            if (idColumn != null) {
                idColumn.setAccessible(true);
                idColumn.set(entity.get(i), resultSet.getObject(1, idColumn.getType()));
            }
            i++;
        }
        return row;
    }

    public Map selectForMap(ResultSet result) throws SQLException {
        Map<String, Object> map = new HashMap<>();
        ResultSetMetaData metaData = result.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            map.put(NameUtils.toHump(metaData.getColumnLabel(i)), result.getObject(i));
        }
        return map;
    }

}
