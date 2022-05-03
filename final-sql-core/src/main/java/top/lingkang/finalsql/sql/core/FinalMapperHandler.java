package top.lingkang.finalsql.sql.core;

import cn.hutool.core.lang.Assert;
import top.lingkang.finalsql.annotation.Insert;
import top.lingkang.finalsql.annotation.Select;
import top.lingkang.finalsql.annotation.Table;
import top.lingkang.finalsql.annotation.Update;
import top.lingkang.finalsql.utils.ClassUtils;

import java.lang.reflect.Method;

/**
 * @author lingkang
 * Created by 2022/5/3
 */
public class FinalMapperHandler {
    protected FinalSqlManage manage;
    protected Class<?> clazz;

    protected FinalMapperHandler(Class<?> clazz, FinalSqlManage manage) {
        this.manage = manage;
        this.clazz = clazz;
    }

    protected Object select(Select select, Method method, Object[] args) {
        Class<?> returnType = ClassUtils.getReturnType(method);
        Assert.notNull(returnType, "查询接口方法不能为 void：" + method.toGenericString());
        Class<?> type = method.getReturnType();
        if ("List".equals(type.getSimpleName())) {
            return manage.selectForList(select.value(), returnType, args);
        } else {
            return manage.selectForObject(select.value(), returnType, args);
        }
    }

    protected Object update(Update update, Method method, Object[] args) {
        int count = manage.nativeUpdate(update.value(), args);
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

}
