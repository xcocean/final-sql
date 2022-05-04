package top.lingkang.finalsql.sql.core;

import cn.hutool.core.lang.Assert;
import top.lingkang.finalsql.annotation.*;
import top.lingkang.finalsql.error.FinalException;
import top.lingkang.finalsql.utils.ClassUtils;

import java.lang.reflect.Method;

/**
 * @author lingkang
 * Created by 2022/5/3
 */
public abstract class AbstractMapperHandler {
    protected FinalSqlManage manage;
    protected Class<?> clazz;

    protected AbstractMapperHandler(Class<?> clazz, FinalSqlManage manage) {
        this.manage = manage;
        this.clazz = clazz;
    }

    protected Object select(Select select, Method method, Object[] args) {
        Class<?> returnType = ClassUtils.getReturnType(method);
        Assert.notNull(returnType, "查询接口方法的返回类型未能识别：" + method.toGenericString());
        Class<?> type = method.getReturnType();
        if ("".equals(select.value())) {
            if (args[0].getClass().getAnnotation(Table.class) != null) {// 映射对象
                return manage.select(args[0]);
            } else
                throw new FinalException("@Select 查询SQL为空时，参数对象应该为表映射实体类！" + args[0]);
        }
        String simpleName = type.getSimpleName();
        if ("List".equals(simpleName)) {
            return manage.selectForList(select.value(), returnType, args);
        } else if ("Map".equals(simpleName)) {
            return manage.selectForMap(select.value(), false, args);
        } else {// 未知
            return manage.selectForObject(select.value(), returnType, args);
        }
    }

    protected Object update(Update update, Method method, Object[] args) {
        int count = 0;
        if ("".equals(update.value())) {
            for (Object obj : args) {
                Table annotation = obj.getClass().getAnnotation(Table.class);
                if (annotation != null) {
                    if (manage.update(obj) > 0) {
                        count++;
                    }
                }
            }
        } else {
            count = manage.nativeUpdate(update.value(), args);
        }
        String type = method.getReturnType().getSimpleName();
        if ("int".equals(type) || "Integer".equals(type)) {
            return count;
        }
        return null;
    }

    protected Object insert(Insert insert, Method method, Object[] args) {
        int count = 0;
        if ("".equals(insert.value())) {
            for (Object obj : args) {
                Table annotation = obj.getClass().getAnnotation(Table.class);
                if (annotation != null) {
                    if (manage.insert(obj) > 0) {
                        count++;
                    }
                }
            }
        } else {
            count = manage.nativeUpdate(insert.value(), args);
        }
        String type = method.getReturnType().getSimpleName();
        if ("int".equals(type) || "Integer".equals(type)) {
            return count;
        }
        return null;
    }

    protected Object delete(Delete delete, Method method, Object[] args) {
        int count = 0;
        if ("".equals(delete.value())) {
            for (Object obj : args) {
                Table annotation = obj.getClass().getAnnotation(Table.class);
                if (annotation != null) {
                    if (manage.delete(obj) > 0) {
                        count++;
                    }
                }
            }
        } else {
            count = manage.nativeUpdate(delete.value(), args);
        }
        String type = method.getReturnType().getSimpleName();
        if ("int".equals(type) || "Integer".equals(type)) {
            return count;
        }
        return null;
    }

}
