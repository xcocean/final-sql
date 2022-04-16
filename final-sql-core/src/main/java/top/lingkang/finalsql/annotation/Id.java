package top.lingkang.finalsql.annotation;

import top.lingkang.finalsql.constants.IdType;

import java.lang.annotation.*;

/**
 * @author lingkang
 * Created by 2022/4/11
 * 声明列为主键ID
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Id {
    IdType value() default IdType.AUTO;
}
