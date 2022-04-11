package top.lingkang.finalsql.utils;

import top.lingkang.finalsql.annotation.Column;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
        return col == null ? null : col.substring(0, col.length() - 2);
    }

    public static List<String> getColumnList(Class<?> t, boolean ignoreAnn) {
        Field[] df = t.getDeclaredFields();
        List<String> list = new ArrayList<>();
        for (Field field : df) {
            Column annotation = field.getAnnotation(Column.class);
            if (ignoreAnn) {
                list.add(field.getName());
            } else if (annotation != null) {
                if ("".equals(annotation.value())) {
                    list.add(field.getName());
                } else {
                    list.add(annotation.value());
                }
            }
        }
        return list;
    }

    public static Field[] getColumnField(Class<?> t, boolean ignoreAnn) {
        Field[] df = t.getDeclaredFields();
        List<Field> list = new ArrayList<>();
        for (Field field : df) {
            Column annotation = field.getAnnotation(Column.class);
            if (ignoreAnn) {
                list.add(field);
            } else if (annotation != null) {
                list.add(field);
            }
        }
        return list.toArray(new Field[list.size()]);
    }
}
