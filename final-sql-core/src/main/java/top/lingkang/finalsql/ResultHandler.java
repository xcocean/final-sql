package top.lingkang.finalsql;


import cn.hutool.core.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;
import top.lingkang.finalsql.annotation.Nullable;
import top.lingkang.finalsql.utils.AnnotationUtils;

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

    public ResultHandler(SqlConfig sqlConfig) {
        this.sqlConfig = sqlConfig;
        if (sqlConfig.isShowResultLog()) {
            log = LoggerFactory.getLogger(ResultHandler.class);
        } else {
            log = NOPLogger.NOP_LOGGER;
        }
    }

    @Nullable
    public <T> List<Object> resultSetToList(ResultSet resultSet, T t) {
        Assert.notNull(resultSet, "ResultSet 结果集不能为空！");
        //光标移到最后
        try {
            if (!resultSet.next()) {
                log.info(null);
                return null;
            } else {
                //光标移到第一条数据前
                resultSet.beforeFirst();
            }

            List<Object> list = new ArrayList<>();

            //获取要封装的javabean声明的属性
            Class<?> clazz = (Class<?>) t;
            Field[] fields = AnnotationUtils.getColumnField((Class<?>) t, false);
            //调用无参构造实例化对象
            Object object = clazz.newInstance();

            //遍历ResultSet
            while (resultSet.next()) {
                //匹配JavaBean的属性,然后赋值
                for (Field field : fields) {
                    field.setAccessible(true);
                    field.set(object, resultSet.getObject(field.getName()));
                }
                list.add(object);
            }
            log.info("result: total: {}\n{}", list.size(), list);
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
