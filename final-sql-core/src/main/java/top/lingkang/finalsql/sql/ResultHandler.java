package top.lingkang.finalsql.sql;


import cn.hutool.core.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;
import top.lingkang.finalsql.annotation.Nullable;
import top.lingkang.finalsql.config.SqlConfig;
import top.lingkang.finalsql.error.ResultHandlerException;
import top.lingkang.finalsql.utils.ClassUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
public class ResultHandler {
    private static Logger log;
    private SqlConfig sqlConfig;

    public ResultHandler(SqlConfig config) {
        this.sqlConfig = config;
        if (sqlConfig.isShowResultLog()) {
            log = LoggerFactory.getLogger(ResultHandler.class);
        } else {
            log = NOPLogger.NOP_LOGGER;
        }
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
                Object obj = clazz.newInstance();
                //匹配JavaBean的属性,然后赋值
                for (Field field : fields) {
                    field.setAccessible(true);
                    field.set(obj, resultSet.getObject(field.getName()));
                }
                list.add((T) obj);
            }
            log.info("select: total: {}\n{}", list.size(), list);
            return list;
        } catch (SQLException | IllegalAccessException | InstantiationException e) {
            throw new ResultHandlerException(e);
        }
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
                field.set(entity, resultSet.getObject(field.getName()));
            }
            log.info("select: total: {}\n{}", 1, entity);
            return entity;
        } catch (SQLException | IllegalAccessException e) {
            throw new ResultHandlerException(e);
        }
    }

    public int count(ResultSet resultSet) {
        try {
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
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
        log.info("insert: total: {}\n{}", row, entity);
        return row;
    }

    public <T> int batchInsert(ResultSet resultSet, List<T> entity) throws SQLException, IllegalAccessException {
        int row = resultSet.getRow();
        int i=0;
        while (resultSet.next()){
            Class<?> clazz = entity.get(i).getClass();
            Field idColumn = ClassUtils.getIdColumn(clazz.getDeclaredFields());
            if (idColumn != null) {
                idColumn.setAccessible(true);
                idColumn.set(entity.get(i), resultSet.getObject(1, idColumn.getType()));
            }
            i++;
        }
        log.info("batchInsert: total: {}\n{}", row, entity);
        return row;
    }


    public <T> int update(ResultSet resultSet, T entity) throws SQLException, IllegalAccessException {
        if (!resultSet.next()) {
            log.info(null);
            return 0;
        }
        Class<?> clazz = entity.getClass();
        Field idColumn = ClassUtils.getIdColumn(clazz.getDeclaredFields());
        if (idColumn != null) {
            idColumn.setAccessible(true);
            idColumn.set(entity, resultSet.getObject(1, idColumn.getType()));
        }
        int row = resultSet.getRow();
        log.info("update: total: {}\n{}", row, entity);
        return row;
    }
}
