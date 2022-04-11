package top.lingkang.finalsql;

import top.lingkang.finalsql.annotation.Table;
import top.lingkang.finalsql.test.TestEnity;
import top.lingkang.finalsql.utils.AnnotationUtils;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
public class SqlGenerate {
    public static <T> String table(T t) {
        String sql = "select ";
        Class<?> clazz = (Class<?>) t;
        String column = AnnotationUtils.getColumn(clazz);
        if (column != null) {
            sql += column;
        } else {
            sql += "*";
        }
        Table annotation = clazz.getAnnotation(Table.class);
        if (annotation != null) {
            return sql + " from " + annotation.value();
        } else {
            return sql + " from " + clazz.getSimpleName();
        }
    }

    public static void main(String[] args) {
        System.out.println(table(TestEnity.class));
    }
}
