package top.lingkang.finalsql.utils;


import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author lingkang
 * date 2022/1/11
 * 实际场景中应该加入cache
 */
public class BeanUtils {

    public static void copyProperty(Object source, Object target, boolean ignoreNullValue) {
        for (Field s : source.getClass().getDeclaredFields()) {
            if (getField(target, s.getName()) != null) {
                try {
                    Object value = getProperty(source, s.getName());
                    if (value == null && ignoreNullValue) {
                        continue;
                    }
                    setProperty(target, s.getName(), value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public static Field getField(Object o, String name) {
        for (Field field : o.getClass().getDeclaredFields()) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    public static void setProperty(Object o, String name, Object value) throws Exception {
        PropertyDescriptor propDesc = new PropertyDescriptor(name, o.getClass());
        Method methodSetUserName = propDesc.getWriteMethod();
        methodSetUserName.invoke(o, value);
    }

    public static Object getProperty(Object o, String name) throws Exception {
        PropertyDescriptor proDescriptor = new PropertyDescriptor(name, o.getClass());
        Method methodGetUserName = proDescriptor.getReadMethod();
        return methodGetUserName.invoke(o);
    }
}
