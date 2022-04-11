package top.lingkang.finalsql.utils;

import top.lingkang.finalsql.annotation.Table;

import java.lang.annotation.Annotation;

/**
 * @author lingkang
 * Created by 2022/4/11
 */
public class ClassUtils {
    public static <T> T getAnnotation(Class obj, T target){
        try {
            return (T) obj.getAnnotation(target.getClass());
        } catch (Exception e) {
        }
        return null;
    }
}
