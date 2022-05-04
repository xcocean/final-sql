package top.lingkang.finalsql.utils;

import cn.hutool.core.util.StrUtil;
import top.lingkang.finalsql.annotation.Column;
import top.lingkang.finalsql.annotation.Id;
import top.lingkang.finalsql.annotation.Nullable;
import top.lingkang.finalsql.error.FinalException;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
public class ClassUtils {
    public static <T> String getColumn(Field[] df) {
        String col = "";
        for (Field field : df) {
            Column annotation = field.getAnnotation(Column.class);
            if (annotation != null) {
                if ("".equals(annotation.value())) {
                    String unHump = NameUtils.unHump(field.getName());
                    if (unHump.equals(field.getName()))
                        col += unHump + ", ";
                    else
                        col += unHump + " as " + field.getName() + ", ";
                } else {
                    col += annotation.value() + ", ";
                }
            }
        }
        return StrUtil.isEmpty(col) ? null : col.substring(0, col.length() - 2);
    }

    public static <T> Field getIdColumn(Field[] df) {
        for (Field field : df) {
            Id annotation = field.getAnnotation(Id.class);
            if (annotation != null) {
                return field;
            }
        }
        return null;
    }

    public static <T> Class<? extends Object> getClass(T entity) {
        if (!(entity instanceof Class)) {
            return entity.getClass();
        }
        try {// 类，需要实例化
            return ((Class<?>) entity).newInstance().getClass();
        } catch (Exception e) {
            throw new FinalException(e);
        }
    }

    public static Field[] getColumnField(Field[] df) {
        List<Field> list = new ArrayList<>();
        for (Field field : df) {
            Column annotation = field.getAnnotation(Column.class);
            if (annotation != null) {
                list.add(field);
            }
        }
        return list.toArray(new Field[list.size()]);
    }

    @Nullable
    public static Field getIdField(Field[] df) {
        for (Field field : df) {
            Id annotation = field.getAnnotation(Id.class);
            if (annotation != null) {
                return field;
            }
        }
        return null;
    }

    @Nullable
    public static Id getId(Field[] df) {
        for (Field field : df) {
            Id annotation = field.getAnnotation(Id.class);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }

    @Nullable
    public static Field getField(String name, Field[] df) {
        for (Field field : df) {
            if (field.getName().equals(name))
                return field;
        }
        return null;
    }

    public static <T> Object getValue(T t, Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(t);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new FinalException(e);
        }
    }

    public static boolean isBaseWrapper(Class o) {
        try {
            return o == String.class ||
                    o == Integer.class ||
                    o == Long.class ||
                    o == Boolean.class ||
                    o == Byte.class ||
                    o == Double.class ||
                    o == Float.class ||
                    o == Short.class;
        } catch (Exception e) {
            return false;
        }
    }

    public static Class<?>[] getClassTypes(Object[] args) {
        Class[] clazz = new Class[args.length];
        for (int i = 0; i < args.length; i++)
            clazz[i] = args[i].getClass();
        return clazz;
    }

    public static <T> T getMapper(Class<T> clazz, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                handler);
    }

    @Nullable
    public static Class<?> getReturnType(Method method) {
        Type returnType = method.getGenericReturnType();
        if (returnType instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) returnType;
            return (Class<?>) type.getActualTypeArguments()[0];
        }
        String typeName = returnType.getTypeName();
        return getBaseClass(typeName);
    }

    @Nullable
    public static Class<?> getBaseClass(String typeName) {
        if ("java.util.Map".equals(typeName)) {
            return Map.class;
        }
        if ("int".equals(typeName) || "java.lang.Integer".equals(typeName)) {
            return Integer.class;
        } else if ("long".equals(typeName) || "java.lang.Long".equals(typeName)) {
            return Long.class;
        } else if ("java.util.Date".equals(typeName)) {
            return Date.class;
        } else if ("void".equals(typeName)) {
            return null;
        }
        return null;
    }
}
