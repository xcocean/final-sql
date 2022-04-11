package top.lingkang.finalsql.utils;

import top.lingkang.finalsql.annotation.Column;

import java.lang.reflect.Field;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
public class AnnotationUtils {
    public static <T> String getColumn(Class<?> t) {
        Field[] df = t.getDeclaredFields();
        String col = "";
        for (Field field : df) {
            Column annotation = field.getAnnotation(Column.class);
            if (annotation != null) {
                if ("".equals(annotation.value())) {
                    col += field.getName() + ", ";
                } else {
                    col += annotation.value() + ", ";
                }
            }
        }
        return col == null ? null : col.substring(0, col.length() - 2);
    }
}
