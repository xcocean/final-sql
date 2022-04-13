package top.lingkang.finalsql;


import cn.hutool.core.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;
import top.lingkang.finalsql.annotation.Nullable;
import top.lingkang.finalsql.error.ResultHandlerException;
import top.lingkang.finalsql.utils.ClassUtils;
import top.lingkang.finalsql.utils.DataSourceUtils;

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
    public <T> List<Object> resultSetToList(ResultSet resultSet, T entity) {
        Assert.notNull(resultSet, "ResultSet 结果集不能为空！");
        try {
            if (!resultSet.next()) {
                log.info(null);
                return null;
            } else {
                // 游标移到第一条数据前
                resultSet.beforeFirst();
            }

            List<Object> list = new ArrayList<>();

            //获取要封装的javabean声明的属性
            Class<?> clazz = entity.getClass();
            Field[] fields = ClassUtils.getColumnField(clazz.getDeclaredFields());
            Object obj = clazz.newInstance();
            //遍历ResultSet
            while (resultSet.next()) {
                //匹配JavaBean的属性,然后赋值
                for (Field field : fields) {
                    field.setAccessible(true);
                    field.set(obj, resultSet.getObject(field.getName()));
                }
                list.add(obj);
            }
            log.info("result: total: {}\n{}", list.size(), list);
            return list;
        } catch (SQLException | IllegalAccessException | InstantiationException e) {
            throw new ResultHandlerException(e);
        } finally {
            DataSourceUtils.close(resultSet);
        }
    }

    public <T> T resultSetToOne(ResultSet resultSet, Object entity) {
        Assert.notNull(resultSet, "ResultSet 结果集不能为空！");
        try {
            if (!resultSet.next()) {
                log.info(null);
                return null;
            }

            //获取要封装的javabean声明的属性
            Class<?> clazz = entity.getClass();
            Field[] fields = ClassUtils.getColumnField(clazz.getDeclaredFields());

            //匹配JavaBean的属性,然后赋值
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(entity, resultSet.getObject(field.getName()));
            }
            log.info("result: total: {}\n{}", 1, entity);
            return (T) entity;
        } catch (SQLException | IllegalAccessException e) {
            throw new ResultHandlerException(e);
        } finally {
            DataSourceUtils.close(resultSet);
        }
    }

    public <T> int resultSetToCount(ResultSet resultSet, Object entity) {
        try {
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new ResultHandlerException(e);
        } finally {
            DataSourceUtils.close(resultSet);
        }
    }
}
