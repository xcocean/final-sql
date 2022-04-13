package top.lingkang.finalsql.utils;

import cn.hutool.core.util.StrUtil;
import top.lingkang.finalsql.annotation.Column;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
public class AnnotationUtils {
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
}
