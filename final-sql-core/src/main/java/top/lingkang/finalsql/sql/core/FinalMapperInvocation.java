package top.lingkang.finalsql.sql.core;

import top.lingkang.finalsql.annotation.Delete;
import top.lingkang.finalsql.annotation.Insert;
import top.lingkang.finalsql.annotation.Select;
import top.lingkang.finalsql.annotation.Update;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author lingkang
 * Created by 2022/5/3
 * 代理映射处理
 */
public class FinalMapperInvocation extends AbstractMapperHandler implements InvocationHandler, Serializable {
    public FinalMapperInvocation(Class<?> clazz, FinalSqlManage manage) {
        super(clazz, manage);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (args != null && args[0].getClass().isArray()) {
            args = (Object[]) args[0];
            if (args.length == 0)
                args = null;
        }

        Select select = method.getAnnotation(Select.class);
        if (select != null)
            return select(select, method, args);
        Update update = method.getAnnotation(Update.class);
        if (update != null)
            return update(update, method, args);
        Insert insert = method.getAnnotation(Insert.class);
        if (insert != null)
            return insert(insert, method, args);
        Delete delete = method.getAnnotation(Delete.class);
        if (delete != null)
            delete(delete, method, args);
        // 无任何操作注解，不做异常处理
        return null;
    }
}
