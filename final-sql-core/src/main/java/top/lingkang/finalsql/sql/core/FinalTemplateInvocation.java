package top.lingkang.finalsql.sql.core;

import top.lingkang.finalsql.annotation.Delete;
import top.lingkang.finalsql.annotation.Insert;
import top.lingkang.finalsql.annotation.Select;
import top.lingkang.finalsql.annotation.Update;
import top.lingkang.finalsql.utils.ClassUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author lingkang
 * Created by 2022/5/3
 * 代理映射处理
 */
public class FinalTemplateInvocation extends AbstractTemplateHandler implements InvocationHandler, Serializable {
    public FinalTemplateInvocation(Class<?> clazz, FinalSqlManage manage) {
        super(clazz, manage);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method exMethod = clazz.getMethod(method.getName(), ClassUtils.getClassTypes(args));
        if (args[0].getClass().isArray()) {
            args = (Object[]) args[0];
            if (args.length == 0)
                args = null;
        }

        Select select = exMethod.getAnnotation(Select.class);
        if (select != null)
            return select(select, method, args);
        Update update = exMethod.getAnnotation(Update.class);
        if (update != null)
            return update(update, exMethod, args);
        Insert insert = exMethod.getAnnotation(Insert.class);
        if (insert != null)
            return insert(insert, exMethod, args);
        Delete delete = exMethod.getAnnotation(Delete.class);
        if (delete != null)
            delete(delete, exMethod, args);
        // 无任何操作注解，不做异常处理
        return null;
    }
}
