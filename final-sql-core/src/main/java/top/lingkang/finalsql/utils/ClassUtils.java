package top.lingkang.finalsql.utils;

import cn.hutool.core.util.StrUtil;
import top.lingkang.finalsql.annotation.Column;
import top.lingkang.finalsql.annotation.Id;
import top.lingkang.finalsql.annotation.Nullable;
import top.lingkang.finalsql.error.FinalException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
        if (!(entity instanceof Class)){
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

    public static <T> Object getValue(T t, Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(t);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new FinalException(e);
        }
    }
}
